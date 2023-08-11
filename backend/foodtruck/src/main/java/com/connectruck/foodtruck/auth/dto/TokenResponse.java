package com.connectruck.foodtruck.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenResponse(
        @Schema(description = "Jwt 토큰", example = "some.jwt.token")
        String accessToken
) {
}
