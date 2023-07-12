package com.connectruck.foodtruck.order.dto;

import com.connectruck.foodtruck.order.domain.OrderLine;
import java.math.BigDecimal;

public record OrderLineResponse(
        Long id,
        Long menuId,
        String name,
        BigDecimal price,
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
