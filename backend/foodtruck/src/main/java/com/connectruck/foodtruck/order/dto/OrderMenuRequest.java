package com.connectruck.foodtruck.order.dto;

public record OrderMenuRequest(
        Long menuId,
        int quantity
) {
}
