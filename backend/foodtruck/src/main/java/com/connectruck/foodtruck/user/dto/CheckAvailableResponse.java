package com.connectruck.foodtruck.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CheckAvailableResponse(
        @Schema(description = "사용 가능 여부", example = "true")
        boolean isAvailable
) {
}
