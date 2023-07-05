package com.connectruck.foodtruck.order.service;

import com.connectruck.foodtruck.event.service.EventService;
import com.connectruck.foodtruck.menu.dto.MenuResponse;
import com.connectruck.foodtruck.menu.service.MenuService;
import com.connectruck.foodtruck.order.domain.OrderInfo;
import com.connectruck.foodtruck.order.domain.OrderInfoRepository;
import com.connectruck.foodtruck.order.domain.OrderLine;
import com.connectruck.foodtruck.order.dto.OrderMenuRequest;
import com.connectruck.foodtruck.order.dto.OrderRequest;
import com.connectruck.foodtruck.order.exception.EventClosedException;
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

    public Long create(final OrderRequest request) {
        final Long participationId = request.truckId();
        checkEventOpened(participationId);

        final OrderInfo orderInfo = OrderInfo.ofNew(request.phone());
        final List<OrderLine> orderLines = request.menus()
                .stream()
                .map(orderMenuRequest -> createOrderLineOf(orderInfo, orderMenuRequest))
                .toList();
        orderInfo.changeOrderLine(orderLines);

        orderInfoRepository.save(orderInfo);
        return orderInfo.getId();
    }

    private void checkEventOpened(final Long participationId) {
        final Long eventId = truckService.findEventIdByParticipationId(participationId);
        if (eventService.isEventClosedAt(eventId, LocalDateTime.now())) {
            throw new EventClosedException();
        }
    }

    private OrderLine createOrderLineOf(final OrderInfo orderInfo, final OrderMenuRequest orderMenuRequest) {
        final Long menuId = orderMenuRequest.menuId();
        final MenuResponse menuResponse = menuService.findById(menuId);

        return OrderLine.ofNew(menuResponse.id(), menuResponse.name(), menuResponse.price(),
                orderMenuRequest.quantity(), orderInfo);
    }
}
