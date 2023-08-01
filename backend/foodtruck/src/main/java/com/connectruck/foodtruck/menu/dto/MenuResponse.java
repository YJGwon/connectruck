package com.connectruck.foodtruck.menu.dto;

import com.connectruck.foodtruck.menu.domain.Menu;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;

public record MenuResponse(
        Long id,
        String name,
        BigDecimal price,
        boolean soldOut,
        String detail,
        @JsonIgnore Long truckId
) {

    public static MenuResponse of(final Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.isSoldOut(),
                menu.getDetail(),
                menu.getTruckId()
        );
    }
}
