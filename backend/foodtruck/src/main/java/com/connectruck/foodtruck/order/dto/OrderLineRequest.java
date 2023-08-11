package com.connectruck.foodtruck.order.dto;

import static com.connectruck.foodtruck.common.validation.ValidationMessage.MISSING_REQUIRED_INPUT;
import static com.connectruck.foodtruck.common.validation.ValidationMessage.SMALLER_THAN_MIN_VALUE;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderLineRequest(
        @Schema(description = "메뉴 id", example = "1")
        @NotNull(message = MISSING_REQUIRED_INPUT + " : 메뉴 id")
        Long menuId,
        @Schema(description = "주문 수량", example = "2")
        @Positive(message = SMALLER_THAN_MIN_VALUE + " : 수량, 최소값 1")
        int quantity
) {
}
