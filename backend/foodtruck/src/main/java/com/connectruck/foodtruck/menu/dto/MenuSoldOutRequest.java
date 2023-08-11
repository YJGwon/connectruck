package com.connectruck.foodtruck.menu.dto;

import static com.connectruck.foodtruck.common.validation.ValidationMessage.MISSING_REQUIRED_INPUT;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record MenuSoldOutRequest(
        @Schema(description = "품절 여부", example = "false")
        @NotNull(message = MISSING_REQUIRED_INPUT)
        Boolean soldOut
) {
}
