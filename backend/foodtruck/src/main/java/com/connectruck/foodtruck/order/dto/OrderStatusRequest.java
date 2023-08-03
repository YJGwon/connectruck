package com.connectruck.foodtruck.order.dto;

import static com.connectruck.foodtruck.common.validation.ValidationMessage.MISSING_REQUIRED_INPUT;

import com.connectruck.foodtruck.order.domain.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderStatusRequest(
        @NotNull(message = MISSING_REQUIRED_INPUT + " : 주문 상태") OrderStatus status
) {
}
