package com.connectruck.foodtruck.notification.dto;

import static com.connectruck.foodtruck.common.validation.ValidationMessage.MISSING_REQUIRED_INPUT;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PushSubscribeRequest(
        @Schema(description = "fcm 기기 등록 토큰")
        @NotNull(message = MISSING_REQUIRED_INPUT)
        String token
) {
}
