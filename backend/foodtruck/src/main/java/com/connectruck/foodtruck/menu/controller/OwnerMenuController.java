package com.connectruck.foodtruck.menu.controller;

import static com.connectruck.foodtruck.user.domain.Role.OWNER;

import com.connectruck.foodtruck.auth.annotation.AuthenticationPrincipal;
import com.connectruck.foodtruck.auth.annotation.Authorization;
import com.connectruck.foodtruck.menu.dto.MenusResponse;
import com.connectruck.foodtruck.menu.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Authorization(OWNER)
@RequestMapping("/api/owner/menus")
@RequiredArgsConstructor
public class OwnerMenuController {

    private final MenuService menuService;

    @Operation(summary = "소유한 푸드트럭 메뉴 목록 조회")
    @GetMapping("/my")
    public MenusResponse findByOwnerId(@AuthenticationPrincipal final Long ownerId) {
        return menuService.findByTruckId(ownerId);
    }
}
