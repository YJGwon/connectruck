package com.connectruck.foodtruck.order.dto;

import com.connectruck.foodtruck.order.domain.OrderInfo;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        Long truckId,
        String phone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<OrderLineResponse> menus
) {

    public static OrderResponse of(final OrderInfo orderInfo) {
        final List<OrderLineResponse> menus = orderInfo.getOrderLines()
                .stream()
                .map(OrderLineResponse::of)
                .toList();

        return new OrderResponse(
                orderInfo.getId(),
                orderInfo.getTruckId(),
                orderInfo.getPhone(),
                orderInfo.getCreatedAt(),
                orderInfo.getUpdatedAt(),
                menus
        );
    }
}
