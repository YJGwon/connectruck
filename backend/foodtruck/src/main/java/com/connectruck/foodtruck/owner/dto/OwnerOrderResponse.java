package com.connectruck.foodtruck.owner.dto;

import com.connectruck.foodtruck.order.domain.OrderInfo;
import java.time.LocalDateTime;

public record OwnerOrderResponse(
        Long id,
        Long truckId,
        String phone,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static OwnerOrderResponse of(final OrderInfo orderInfo) {
        return new OwnerOrderResponse(
                orderInfo.getId(),
                orderInfo.getTruckId(),
                orderInfo.getPhone(),
                orderInfo.getStatus().toKorean(),
                orderInfo.getCreatedAt(),
                orderInfo.getUpdatedAt()
        );
    }
}
