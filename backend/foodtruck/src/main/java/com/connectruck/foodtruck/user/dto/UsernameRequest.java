package com.connectruck.foodtruck.user.dto;

import static com.connectruck.foodtruck.common.validation.ValidationMessage.MISSING_REQUIRED_INPUT;

import jakarta.validation.constraints.NotBlank;

public record UsernameRequest(@NotBlank(message = MISSING_REQUIRED_INPUT + " : 아이디") String username) {
}
