package com.connectruck.foodtruck.order.dto;

import java.util.List;

public record OrderRequest(
        Long truckId,
        List<OrderMenuRequest> menus) {
}
