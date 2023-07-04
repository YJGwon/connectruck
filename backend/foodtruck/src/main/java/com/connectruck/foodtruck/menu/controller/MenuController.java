package com.connectruck.foodtruck.menu.controller;

import com.connectruck.foodtruck.menu.dto.MenusResponse;
import com.connectruck.foodtruck.menu.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trucks/{participationId}/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @Operation(summary = "푸드트럭 메뉴 조회")
    @GetMapping
    public MenusResponse findAll(@PathVariable final long participationId) {
        return menuService.findByParticipationId(participationId);
    }
}
