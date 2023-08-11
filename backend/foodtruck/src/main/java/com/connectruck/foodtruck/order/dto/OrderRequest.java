package com.connectruck.foodtruck.order.dto;

import static com.connectruck.foodtruck.common.validation.ValidationMessage.INVALID_FORMAT;
import static com.connectruck.foodtruck.common.validation.ValidationMessage.MISSING_REQUIRED_INPUT;

import com.connectruck.foodtruck.common.validation.FormatText;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public record OrderRequest(
        @Schema(description = "푸드트럭 id", example = "1")
        @NotNull(message = MISSING_REQUIRED_INPUT + " : 푸드트럭 id")
        Long truckId,
        @Schema(description = "휴대폰 번호", example = "01000000000")
        @Pattern(regexp = FormatText.PHONE, message = INVALID_FORMAT + " : 휴대폰 번호")
        String phone,
        @NotEmpty(message = MISSING_REQUIRED_INPUT + " : 주문 메뉴")
        List<@Valid OrderLineRequest> menus
) {
}
