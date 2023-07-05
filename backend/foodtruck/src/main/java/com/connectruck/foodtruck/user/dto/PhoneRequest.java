package com.connectruck.foodtruck.user.dto;

import static com.connectruck.foodtruck.common.validation.ValidationMessage.INVALID_FORMAT;

import com.connectruck.foodtruck.common.validation.FormatText;
import jakarta.validation.constraints.Pattern;

public record PhoneRequest(
        @Pattern(regexp = FormatText.PHONE, message = INVALID_FORMAT + " : 휴대폰 번호") String phone
) {
}
