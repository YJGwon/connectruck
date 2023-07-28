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
import com.connectruck.foodtruck.order.dto.OrdererInfoRequest;
import com.connectruck.foodtruck.order.dto.OrdersResponse;
import com.connectruck.foodtruck.order.exception.NotOwnerOfOrderException;
import com.connectruck.foodtruck.order.exception.OrderCreationException;
import com.connectruck.foodtruck.order.infra.OrderCreatedMessage;
import com.connectruck.foodtruck.order.infra.OrderMessagePublisher;
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

        publishOrderCreatedMessage(truckId, id);
        return id;
    }

    public OrderDetailResponse findByIdAndOrdererInfo(final Long id, final OrdererInfoRequest request) {
        final OrderInfo found = orderInfoRepository.findByIdAndPhone(id, request.phone())
                .orElseThrow(() -> new ClientException("주문 정보를 조회할 수 없습니다.", "잘못된 주문 정보 입니다."));
        final Truck orderedTruck = truckRepository.findById(found.getTruckId())
                .orElse(Truck.NULL);
        final Event orderedEvent = eventRepository.findById(orderedTruck.getEventId())
                .orElse(Event.NULL);

        return OrderDetailResponse.of(found, orderedTruck, orderedEvent);
    }

    public OrderResponse findByIdAndOwnerId(final Long id, final Long ownerId) {
        final OrderInfo found = getOneById(id);
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
    public void acceptOrder(final Long id, final Long ownerId) {
        final OrderInfo order = getOneById(id);
        checkOwnerOfOrder(order, ownerId);
        order.accept();
    }

    @Transactional
    public void finishCooking(final Long id, final Long ownerId) {
        final OrderInfo order = getOneById(id);
        checkOwnerOfOrder(order, ownerId);
        order.finishCooking();
    }

    @Transactional
    public void complete(final Long id, final Long ownerId) {
        final OrderInfo order = getOneById(id);
        checkOwnerOfOrder(order, ownerId);
        order.complete();
    }

    @Transactional
    public void cancel(Long id, Long ownerId) {
        final OrderInfo order = getOneById(id);
        checkOwnerOfOrder(order, ownerId);
        order.cancel();
    }

    private OrderLine createOrderLineOf(final OrderInfo orderInfo, final OrderLineRequest orderLineRequest) {
        final Long menuId = orderLineRequest.menuId();
        final Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> NotFoundException.of("메뉴", "menuId", menuId));
        checkTruckHasMenu(orderInfo, menu);

        return OrderLine.ofNew(menu.getId(), menu.getName(), menu.getPrice(), orderLineRequest.quantity(), orderInfo);
    }

    private void publishOrderCreatedMessage(Long truckId, Long id) {
        try {
            orderMessagePublisher.publishCreatedMessage(new OrderCreatedMessage(truckId, id));
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
            throw OrderCreationException.ofClosed();
        }
    }

    private void checkTruckHasMenu(final OrderInfo orderInfo, final Menu menu) {
        if (!menu.isTruckId(orderInfo.getTruckId())) {
            throw OrderCreationException.ofOtherTruck();
        }
    }

    private void checkOwnerOfOrder(final OrderInfo order, final Long ownerId) {
        final Long truckId = getTruckIdByOwnerId(ownerId);

        if (!order.isTruckId(truckId)) {
            throw new NotOwnerOfOrderException();
        }
    }

    private OrderInfo getOneById(final Long id) {
        return orderInfoRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("주문 정보", "orderId", id));
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
