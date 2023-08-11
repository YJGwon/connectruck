package com.connectruck.foodtruck.menu.controller;

import com.connectruck.foodtruck.menu.dto.MenusResponse;
import com.connectruck.foodtruck.menu.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "menu", description = "메뉴 관련 API")
@RestController
@RequestMapping("/api/trucks/{truckId}/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @Operation(summary = "푸드트럭 메뉴 목록 조회")
    @GetMapping
    public MenusResponse findByTruckId(@PathVariable final long truckId) {
        return menuService.findByTruckId(truckId);
    }
}
