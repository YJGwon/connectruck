package com.connectruck.foodtruck.auth.dto;

import static com.connectruck.foodtruck.common.constant.ValidationMessage.MISSING_REQUIRED_INPUT;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank(message = MISSING_REQUIRED_INPUT + " : 아이디") String username,
        @NotBlank(message = MISSING_REQUIRED_INPUT + " : 비밀번호") String password
) {
}
