package com.connectruck.foodtruck.order.dto;

import static com.connectruck.foodtruck.common.validation.ValidationMessage.INVALID_FORMAT;
import static com.connectruck.foodtruck.common.validation.ValidationMessage.MISSING_REQUIRED_INPUT;

import com.connectruck.foodtruck.common.validation.FormatText;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public record OrderRequest(
        @NotNull(message = MISSING_REQUIRED_INPUT + " : 푸드트럭 id") Long truckId,
        @Pattern(regexp = FormatText.PHONE, message = INVALID_FORMAT + " : 휴대폰 번호") String phone,
        @NotEmpty(message = MISSING_REQUIRED_INPUT + " : 주문 메뉴") List<@Valid OrderMenuRequest> menus) {
}
