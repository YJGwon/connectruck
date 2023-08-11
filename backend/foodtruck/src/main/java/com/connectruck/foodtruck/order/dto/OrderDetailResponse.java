package com.connectruck.foodtruck.order.dto;

import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.order.domain.OrderInfo;
import com.connectruck.foodtruck.truck.domain.Truck;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailResponse(
        @Schema(description = "주문 id", example = "1")
        Long id,
        OrderedEventResponse event,
        OrderedTruckResponse truck,
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
