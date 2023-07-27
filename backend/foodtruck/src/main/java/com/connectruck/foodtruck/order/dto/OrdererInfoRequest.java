package com.connectruck.foodtruck.order.dto;

import static com.connectruck.foodtruck.common.validation.ValidationMessage.INVALID_FORMAT;

import com.connectruck.foodtruck.common.validation.FormatText;
import jakarta.validation.constraints.Pattern;

public record OrdererInfoRequest(
        @Pattern(regexp = FormatText.PHONE, message = INVALID_FORMAT + " : 휴대폰 번호") String phone
) {
}
