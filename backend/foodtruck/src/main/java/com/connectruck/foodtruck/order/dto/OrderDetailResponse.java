package com.connectruck.foodtruck.order.dto;

import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.order.domain.OrderInfo;
import com.connectruck.foodtruck.truck.domain.Truck;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailResponse(
        Long id,
        OrderedEventResponse event,
        OrderedTruckResponse truck,
        String phone,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<OrderLineResponse> menus
) {

    public static OrderDetailResponse of(final OrderInfo orderInfo, final Truck orderedTruck, final Event event) {
        final List<OrderLineResponse> menus = orderInfo.getOrderLines()
                .stream()
                .map(OrderLineResponse::of)
                .toList();

        return new OrderDetailResponse(
                orderInfo.getId(),
                OrderedEventResponse.of(event),
                OrderedTruckResponse.of(orderedTruck),
                orderInfo.getPhone(),
                orderInfo.getStatus().toKorean(),
                orderInfo.getCreatedAt(),
                orderInfo.getUpdatedAt(),
                menus
        );
    }
}
