package com.connectruck.foodtruck.menu.dto;

import com.connectruck.foodtruck.menu.domain.Menu;

public record MenuResponse(
        Long id,
        String name,
        int price
) {

    public static MenuResponse of(final Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice().intValue());
    }
}
