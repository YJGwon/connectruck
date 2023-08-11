package com.connectruck.foodtruck.menu.controller;

import static com.connectruck.foodtruck.user.domain.Role.OWNER;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.connectruck.foodtruck.auth.annotation.AuthenticationPrincipal;
import com.connectruck.foodtruck.auth.annotation.Authorization;
import com.connectruck.foodtruck.menu.dto.MenuDescriptionRequest;
import com.connectruck.foodtruck.menu.dto.MenuSoldOutRequest;
import com.connectruck.foodtruck.menu.dto.MenusResponse;
import com.connectruck.foodtruck.menu.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "owner menu", description = "메뉴 관련 사장님 API")
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

    @Operation(summary = "소유한 푸드트럭 메뉴 설명 수정")
    @PutMapping("/{menuId}/description")
    @ResponseStatus(NO_CONTENT)
    public void updateDescription(@RequestBody @Valid final MenuDescriptionRequest request,
                                  @PathVariable final Long menuId,
                                  @AuthenticationPrincipal final Long ownerId) {
        menuService.updateDescription(request, menuId, ownerId);
    }

    @Operation(summary = "소유한 푸드트럭 품절 상태 수정")
    @PutMapping("/{menuId}/sold-out")
    @ResponseStatus(NO_CONTENT)
    public void updateSoldOut(@RequestBody @Valid final MenuSoldOutRequest request,
                              @PathVariable final Long menuId,
                              @AuthenticationPrincipal final Long ownerId) {
        menuService.updateSoldOut(request, menuId, ownerId);
    }
}
