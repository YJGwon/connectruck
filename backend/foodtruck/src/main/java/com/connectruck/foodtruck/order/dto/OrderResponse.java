package com.connectruck.foodtruck.order.dto;

import com.connectruck.foodtruck.order.domain.OrderInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        @Schema(description = "주문 id", example = "1")
        Long id,
        @Schema(description = "푸드트럭 id", example = "1")
        Long truckId,
        @Schema(description = "휴대폰 번호", example = "01000000000")
        String phone,
        @Schema(description = "주문 처리 상태", example = "접수 대기")
        String status,
        @Schema(description = "주문 시각", example = "")
        LocalDateTime createdAt,
        @Schema(description = "마지막 처리 시각", example = "")
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
                orderInfo.getStatus().toKorean(),
                orderInfo.getCreatedAt(),
                orderInfo.getUpdatedAt(),
                menus
        );
    }
}
