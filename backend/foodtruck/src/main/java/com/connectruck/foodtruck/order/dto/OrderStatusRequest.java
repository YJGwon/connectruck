package com.connectruck.foodtruck.order.dto;

import static com.connectruck.foodtruck.common.validation.ValidationMessage.MISSING_REQUIRED_INPUT;

import com.connectruck.foodtruck.order.domain.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record OrderStatusRequest(
        @Schema(description = "주문 처리 상태", example = "accepted")
        @NotNull(message = MISSING_REQUIRED_INPUT + " : 주문 상태")
        OrderStatus status
) {
}
