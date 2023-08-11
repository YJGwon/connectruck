package com.connectruck.foodtruck.order.dto;

import static com.connectruck.foodtruck.common.validation.ValidationMessage.INVALID_FORMAT;

import com.connectruck.foodtruck.common.validation.FormatText;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public record OrdererInfoRequest(
        @Schema(description = "휴대폰 번호", example = "01000000000")
        @Pattern(regexp = FormatText.PHONE, message = INVALID_FORMAT + " : 휴대폰 번호")
        String phone
) {
}
