package com.connectruck.foodtruck.user.dto;

import static com.connectruck.foodtruck.common.validation.FormatText.PASSWORD_DESCRIPTION;
import static com.connectruck.foodtruck.common.validation.ValidationMessage.INVALID_FORMAT;
import static com.connectruck.foodtruck.common.validation.ValidationMessage.MISSING_REQUIRED_INPUT;

import com.connectruck.foodtruck.common.validation.FormatText;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserRequest(
        @Schema(description = "아이디", example = "example")
        @NotBlank(message = MISSING_REQUIRED_INPUT + " : 아이디")
        String username,
        @Schema(description = "비밀번호", example = "password1234!")
        @Pattern(regexp = FormatText.PASSWORD, message = INVALID_FORMAT + " : 비밀번호 - " + PASSWORD_DESCRIPTION)
        String password,
        @Schema(description = "휴대폰 번호", example = "01000000000")
        @Pattern(regexp = FormatText.PHONE, message = INVALID_FORMAT + " : 휴대폰 번호")
        String phone,
        @Schema(description = "계정 권한", example = "owner")
        @NotNull(message = MISSING_REQUIRED_INPUT + " : 계정 권한")
        Role role
) {

    public Account toEntity() {
        return Account.ofNew(username, password, phone, role);
    }
}
