package com.connectruck.foodtruck.menu.dto;

import com.connectruck.foodtruck.menu.domain.Menu;
import java.math.BigDecimal;

public record MenuResponse(
        Long id,
        String name,
        BigDecimal price
) {

    public static MenuResponse of(final Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice());
    }
}
