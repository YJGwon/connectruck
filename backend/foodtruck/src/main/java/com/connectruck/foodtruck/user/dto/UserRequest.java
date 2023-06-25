package com.connectruck.foodtruck.user.dto;

import static com.connectruck.foodtruck.common.constant.FormatText.PASSWORD_TO_MESSAGE;
import static com.connectruck.foodtruck.common.constant.ValidationMessage.INVALID_FORMAT;
import static com.connectruck.foodtruck.common.constant.ValidationMessage.MISSING_REQUIRED_INPUT;

import com.connectruck.foodtruck.common.constant.FormatText;
import com.connectruck.foodtruck.user.domain.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserRequest(
        @NotBlank(message = MISSING_REQUIRED_INPUT + " : 닉네임") String username,
        @Pattern(regexp = FormatText.PASSWORD,
                message = INVALID_FORMAT + " : 비밀번호 - " + PASSWORD_TO_MESSAGE) String password,
        @Pattern(regexp = FormatText.PHONE, message = INVALID_FORMAT + " : 휴대폰 번호") String phone,
        @NotNull(message = MISSING_REQUIRED_INPUT + " : 계정 권한") Role role
) {
}
