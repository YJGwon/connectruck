package com.connectruck.foodtruck.user.dto;

import static com.connectruck.foodtruck.common.validation.ValidationMessage.MISSING_REQUIRED_INPUT;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UsernameRequest(
        @Schema(description = "아이디", example = "example")
        @NotBlank(message = MISSING_REQUIRED_INPUT + " : 아이디")
        String username
) {
}
