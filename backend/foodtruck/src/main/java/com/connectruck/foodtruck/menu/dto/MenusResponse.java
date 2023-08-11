package com.connectruck.foodtruck.menu.dto;

import com.connectruck.foodtruck.menu.domain.Menu;
import java.util.List;

public record MenusResponse(List<MenuResponse> menus) {

    public static MenusResponse of(final List<Menu> menus) {
        final List<MenuResponse> menuResponses = menus.stream()
                .map(MenuResponse::of)
                .toList();
        return new MenusResponse(menuResponses);
    }
}
