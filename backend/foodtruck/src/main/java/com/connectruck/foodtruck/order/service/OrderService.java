package com.connectruck.foodtruck.order.service;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.event.service.EventService;
import com.connectruck.foodtruck.menu.dto.MenuResponse;
import com.connectruck.foodtruck.menu.service.MenuService;
import com.connectruck.foodtruck.notification.service.NotificationService;
import com.connectruck.foodtruck.order.domain.OrderInfo;
import com.connectruck.foodtruck.order.domain.OrderInfoRepository;
import com.connectruck.foodtruck.order.domain.OrderLine;
import com.connectruck.foodtruck.order.domain.OrderStatus;
import com.connectruck.foodtruck.order.dto.OrderLineRequest;
import com.connectruck.foodtruck.order.dto.OrderRequest;
import com.connectruck.foodtruck.order.dto.OrderResponse;
import com.connectruck.foodtruck.order.dto.OrdersResponse;
import com.connectruck.foodtruck.order.exception.NotOwnerOfOrderException;
import com.connectruck.foodtruck.order.exception.OrderCreationException;
import com.connectruck.foodtruck.truck.service.TruckService;
import java.time.LocalDateTime;
import java.util.List;
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

    private final MenuService menuService;
    private final TruckService truckService;
    private final EventService eventService;
    private final NotificationService notificationService;

    private static void checkTruckHasMenu(final OrderInfo orderInfo, final MenuResponse menuResponse) {
        if (!orderInfo.getTruckId().equals(menuResponse.truckId())) {
            throw OrderCreationException.ofOtherTruck();
        }
    }

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

        notifyNewOrderCreated(truckId, id);
        return id;
    }

    public OrderResponse findById(final Long id) {
        final OrderInfo found = getOneById(id);

        return OrderResponse.of(found);
    }

    public OrdersResponse findOrdersByOwnerIdAndStatus(final Long ownerId, final String rawStatus,
                                                       final int page, final int size) {
        final Long truckId = truckService.findByOwnerId(ownerId).id();

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

    private void checkEventOpened(final Long truckId) {
        final Long eventId = truckService.findEventIdById(truckId);
        if (eventService.isEventClosedAt(eventId, LocalDateTime.now())) {
            throw OrderCreationException.ofClosed();
        }
    }

    private OrderLine createOrderLineOf(final OrderInfo orderInfo, final OrderLineRequest orderLineRequest) {
        final Long menuId = orderLineRequest.menuId();
        final MenuResponse menuResponse = menuService.findById(menuId);
        checkTruckHasMenu(orderInfo, menuResponse);

        return OrderLine.ofNew(menuResponse.id(), menuResponse.name(), menuResponse.price(),
                orderLineRequest.quantity(), orderInfo);
    }

    private void notifyNewOrderCreated(Long truckId, Long id) {
        try {
            notificationService.notifyOrderCreated(truckId, id);
        } catch (Exception e) {
            log.error("Order notification failed", e);
        }
    }

    private void checkOwnerOfOrder(final OrderInfo order, final Long ownerId) {
        if (!ownerId.equals(truckService.findOwnerIdById(order.getTruckId()))) {
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
}
