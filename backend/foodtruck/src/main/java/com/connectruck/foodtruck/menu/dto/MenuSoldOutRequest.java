package com.connectruck.foodtruck.menu.dto;

import static com.connectruck.foodtruck.common.validation.ValidationMessage.MISSING_REQUIRED_INPUT;

import jakarta.validation.constraints.NotNull;

public record MenuSoldOutRequest(
        @NotNull(message = MISSING_REQUIRED_INPUT) Boolean soldOut
) {
}
