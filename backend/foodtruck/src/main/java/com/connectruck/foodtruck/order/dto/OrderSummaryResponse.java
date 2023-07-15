package com.connectruck.foodtruck.order.dto;

import com.connectruck.foodtruck.order.domain.OrderInfo;
import java.time.LocalDateTime;

public record OrderSummaryResponse(
        Long id,
        Long truckId,
        String phone,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static OrderSummaryResponse of(final OrderInfo orderInfo) {
        return new OrderSummaryResponse(
                orderInfo.getId(),
                orderInfo.getTruckId(),
                orderInfo.getPhone(),
                orderInfo.getStatus().toKorean(),
                orderInfo.getCreatedAt(),
                orderInfo.getUpdatedAt()
        );
    }
}
