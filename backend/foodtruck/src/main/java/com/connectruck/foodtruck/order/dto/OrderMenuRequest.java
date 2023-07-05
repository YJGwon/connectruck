package com.connectruck.foodtruck.order.dto;

import static com.connectruck.foodtruck.common.constant.ValidationMessage.MISSING_REQUIRED_INPUT;
import static com.connectruck.foodtruck.common.constant.ValidationMessage.SMALLER_THAN_MIN_VALUE;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderMenuRequest(
        @NotNull(message = MISSING_REQUIRED_INPUT + " : 메뉴 id") Long menuId,
        @Positive(message = SMALLER_THAN_MIN_VALUE + " : 수량, 최소값 1") int quantity
) {
}
