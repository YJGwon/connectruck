package com.connectruck.foodtruck.order.dto;

import com.connectruck.foodtruck.order.domain.OrderInfo;
import com.connectruck.foodtruck.truck.dto.TruckResponse;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailResponse(
        Long id,
        TruckResponse truck,
        String phone,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<OrderLineResponse> menus
) {

    public static OrderDetailResponse of(final TruckResponse truck, final OrderInfo orderInfo) {
        final List<OrderLineResponse> menus = orderInfo.getOrderLines()
                .stream()
                .map(OrderLineResponse::of)
                .toList();

        return new OrderDetailResponse(
                orderInfo.getId(),
                truck,
                orderInfo.getPhone(),
                orderInfo.getStatus().toKorean(),
                orderInfo.getCreatedAt(),
                orderInfo.getUpdatedAt(),
                menus
        );
    }
}
