package com.connectruck.foodtruck.order.dto;

import com.connectruck.foodtruck.order.domain.OrderLine;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record OrderLineResponse(
        @Schema(description = "주문 항목 id", example = "1")
        Long id,
        @Schema(description = "메뉴 id", example = "1")
        Long menuId,
        @Schema(description = "메뉴 이름", example = "핫도그")
        String name,
        @Schema(description = "메뉴 가격", example = "4500")
        BigDecimal price,
        @Schema(description = "주문 수량", example = "2")
        int quantity
) {

    public static OrderLineResponse of(final OrderLine orderLine) {
        return new OrderLineResponse(
                orderLine.getId(),
                orderLine.getMenuId(),
                orderLine.getMenuName(),
                orderLine.getMenuPrice(),
                orderLine.getQuantity()
        );
    }
}
