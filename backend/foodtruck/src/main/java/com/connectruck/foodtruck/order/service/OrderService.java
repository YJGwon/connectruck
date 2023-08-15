package com.connectruck.foodtruck.order.service;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.connectruck.foodtruck.common.exception.ClientException;
import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.event.domain.EventRepository;
import com.connectruck.foodtruck.event.domain.Schedule;
import com.connectruck.foodtruck.event.domain.ScheduleRepository;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.menu.domain.MenuRepository;
import com.connectruck.foodtruck.order.domain.OrderInfo;
import com.connectruck.foodtruck.order.domain.OrderInfoRepository;
import com.connectruck.foodtruck.order.domain.OrderLine;
import com.connectruck.foodtruck.order.domain.OrderStatus;
import com.connectruck.foodtruck.order.dto.OrderDetailResponse;
import com.connectruck.foodtruck.order.dto.OrderLineRequest;
import com.connectruck.foodtruck.order.dto.OrderRequest;
import com.connectruck.foodtruck.order.dto.OrderResponse;
import com.connectruck.foodtruck.order.dto.OrderStatusRequest;
import com.connectruck.foodtruck.order.dto.OrdererInfoRequest;
import com.connectruck.foodtruck.order.dto.OrdersResponse;
import com.connectruck.foodtruck.order.exception.OrderCreationException;
import com.connectruck.foodtruck.order.exception.WrongOrderInfoException;
import com.connectruck.foodtruck.order.message.OrderMessage;
import com.connectruck.foodtruck.order.message.OrderMessagePublisher;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.truck.domain.TruckRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderInfoRepository orderInfoRepository;
    private final MenuRepository menuRepository;
    private final TruckRepository truckRepository;
    private final EventRepository eventRepository;
    private final ScheduleRepository scheduleRepository;

    private final OrderMessagePublisher orderMessagePublisher;

    @Transactional
    public Long create(final OrderRequest request) {
        final Long truckId = request.truckId();
        checkEventOpened(truckId);

        final OrderInfo orderInfo = OrderInfo.ofNew(truckId, request.phone());
        final List<OrderLine> orderLines = request.menus()
                .stream()
                .map(orderMenuRequest -> createOrderLineOf(orderInfo, orderMenuRequest))
                .toList();
        orderInfo.changeOrderLine(orderLines);

        orderInfoRepository.save(orderInfo);
        final Long id = orderInfo.getId();

        publishOrderMessage(orderInfo);
        return id;
    }

    public OrderDetailResponse findByIdAndOrdererInfo(final Long id, final OrdererInfoRequest request) {
        final Optional<OrderInfo> found = orderInfoRepository.findById(id);
        if (found.isEmpty() || !found.get().isPhone(request.phone())) {
            throw new WrongOrderInfoException();
        }

        final OrderInfo order = found.get();
        final Truck orderedTruck = truckRepository.findById(order.getTruckId())
                .orElse(Truck.NULL);
        final Event orderedEvent = eventRepository.findById(orderedTruck.getEventId())
                .orElse(Event.NULL);

        return OrderDetailResponse.of(order, orderedTruck, orderedEvent);
    }

    @Transactional
    public void cancel(final Long id, final OrdererInfoRequest request) {
        final Optional<OrderInfo> found = orderInfoRepository.findForUpdateById(id);
        if (found.isEmpty() || !found.get().isPhone(request.phone())) {
            throw new WrongOrderInfoException();
        }

        final OrderInfo order = found.get();
        if (order.isAccepted()) {
            throw new ClientException("주문을 취소할 수 없습니다.", "접수된 주문은 취소할 수 없습니다.");
        }
        order.changeStatus(OrderStatus.CANCELED);
    }

    public OrderResponse findByIdAndOwnerId(final Long id, final Long ownerId) {
        final OrderInfo found = orderInfoRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("주문 정보", "orderId", id));
        checkOwnerOfOrder(found, ownerId);
        return OrderResponse.of(found);
    }

    public OrdersResponse findOrdersByOwnerIdAndStatus(final Long ownerId, final String rawStatus,
                                                       final int page, final int size) {
        final Long truckId = getTruckIdByOwnerId(ownerId);

        final Sort latest = Sort.by(DESC, "createdAt");
        final PageRequest pageRequest = PageRequest.of(page, size, latest);

        final OrderStatus status = OrderStatus.valueOf(rawStatus.toUpperCase());
        final Page<OrderInfo> found = getOrdersByTruckIdAndStatus(status, truckId, pageRequest);
        return OrdersResponse.of(found);
    }

    @Transactional
    public void changeStatus(final OrderStatusRequest request, final Long id, final Long ownerId) {
        final OrderInfo order = orderInfoRepository.findForUpdateById(id)
                .orElseThrow(() -> NotFoundException.of("주문 정보", "orderId", id));
        checkOwnerOfOrder(order, ownerId);
        order.changeStatus(request.status());
    }

    private OrderLine createOrderLineOf(final OrderInfo orderInfo, final OrderLineRequest orderLineRequest) {
        final Long menuId = orderLineRequest.menuId();
        final Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> NotFoundException.of("메뉴", "menuId", menuId));
        checkTruckHasMenu(orderInfo, menu);
        checkMenuAvailable(menu);

        return OrderLine.ofNew(menu.getId(), menu.getName(), menu.getPrice(), orderLineRequest.quantity(), orderInfo);
    }

    private void publishOrderMessage(final OrderInfo orderInfo) {
        try {
            orderMessagePublisher.publish(OrderMessage.of(orderInfo));
        } catch (Exception e) {
            log.error("failed to send order created message", e);
        }
    }

    private void checkEventOpened(final Long truckId) {
        final Long eventId = truckRepository.findById(truckId)
                .orElseThrow(() -> NotFoundException.of("푸드트럭", "truckId", truckId))
                .getEventId();
        final LocalDateTime now = LocalDateTime.now();

        final Optional<Schedule> scheduleToday =
                scheduleRepository.findByEventIdAndEventDate(eventId, now.toLocalDate());
        if (scheduleToday.isEmpty() || scheduleToday.get().isClosedAt(now.toLocalTime())) {
            throw new OrderCreationException("푸드트럭 운영 시간이 아닐 때에는 주문할 수 없습니다.");
        }
    }

    private void checkTruckHasMenu(final OrderInfo orderInfo, final Menu menu) {
        if (!menu.isTruckId(orderInfo.getTruckId())) {
            throw new OrderCreationException("다른 푸드트럭의 메뉴는 주문할 수 없습니다.");
        }
    }

    private void checkMenuAvailable(final Menu menu) {
        if (menu.isSoldOut()) {
            throw new OrderCreationException("메뉴가 품절되어 주문할 수 없습니다.");
        }
    }

    private void checkOwnerOfOrder(final OrderInfo order, final Long ownerId) {
        final Long truckId = getTruckIdByOwnerId(ownerId);

        if (!order.isTruckId(truckId)) {
            throw new ClientException("소유하지 않은 푸드트럭의 주문입니다.", "소유하지 않은 푸드트럭의 주문을 처리할 수 없습니다.");
        }
    }

    private Page<OrderInfo> getOrdersByTruckIdAndStatus(final OrderStatus status, final Long truckId,
                                                        final PageRequest pageRequest) {
        if (status == OrderStatus.ALL) {
            return orderInfoRepository.findByTruckId(truckId, pageRequest);
        }
        return orderInfoRepository.findByTruckIdAndStatus(truckId, status, pageRequest);
    }

    private Long getTruckIdByOwnerId(Long ownerId) {
        return truckRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> NotFoundException.of("소유한 푸드트럭", "ownerId", ownerId))
                .getId();
    }
}
