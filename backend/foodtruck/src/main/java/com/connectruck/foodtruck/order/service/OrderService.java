package com.connectruck.foodtruck.order.service;

import com.connectruck.foodtruck.event.service.EventService;
import com.connectruck.foodtruck.menu.dto.MenuResponse;
import com.connectruck.foodtruck.menu.service.MenuService;
import com.connectruck.foodtruck.order.domain.OrderInfo;
import com.connectruck.foodtruck.order.domain.OrderInfoRepository;
import com.connectruck.foodtruck.order.domain.OrderLine;
import com.connectruck.foodtruck.order.dto.OrderLineRequest;
import com.connectruck.foodtruck.order.dto.OrderRequest;
import com.connectruck.foodtruck.order.exception.OrderCreationException;
import com.connectruck.foodtruck.truck.service.TruckService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderInfoRepository orderInfoRepository;

    private final MenuService menuService;
    private final TruckService truckService;
    private final EventService eventService;

    private static void checkTruckHasMenu(final OrderInfo orderInfo, final MenuResponse menuResponse) {
        if (!orderInfo.getTruckId().equals(menuResponse.truckId())) {
            throw OrderCreationException.ofOtherTruck();
        }
    }

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
        return orderInfo.getId();
    }

    private OrderLine createOrderLineOf(final OrderInfo orderInfo, final OrderLineRequest orderLineRequest) {
        final Long menuId = orderLineRequest.menuId();
        final MenuResponse menuResponse = menuService.findById(menuId);
        checkTruckHasMenu(orderInfo, menuResponse);

        return OrderLine.ofNew(menuResponse.id(), menuResponse.name(), menuResponse.price(),
                orderLineRequest.quantity(), orderInfo);
    }

    private void checkEventOpened(final Long truckId) {
        final Long eventId = truckService.findEventIdById(truckId);
        if (eventService.isEventClosedAt(eventId, LocalDateTime.now())) {
            throw OrderCreationException.ofClosed();
        }
    }
}
