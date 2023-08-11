package com.connectruck.foodtruck.user.dto;

import static com.connectruck.foodtruck.common.validation.ValidationMessage.INVALID_FORMAT;

import com.connectruck.foodtruck.common.validation.FormatText;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public record PhoneRequest(
        @Schema(description = "휴대폰 번호", example = "01000000000")
        @Pattern(regexp = FormatText.PHONE, message = INVALID_FORMAT + " : 휴대폰 번호")
        String phone
) {
}
