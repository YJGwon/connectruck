package com.connectruck.foodtruck.order.dto;

import static com.connectruck.foodtruck.common.constant.ValidationMessage.MISSING_REQUIRED_INPUT;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record OrderRequest(
        @NotNull(message = MISSING_REQUIRED_INPUT + " : 푸드트럭 id") Long truckId,
        @NotEmpty(message = MISSING_REQUIRED_INPUT + " : 주문 메뉴") List<@Valid OrderMenuRequest> menus) {
}
