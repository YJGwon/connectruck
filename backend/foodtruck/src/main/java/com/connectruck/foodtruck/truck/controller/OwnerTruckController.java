package com.connectruck.foodtruck.truck.controller;

import static com.connectruck.foodtruck.user.domain.Role.OWNER;

import com.connectruck.foodtruck.auth.annotation.AuthenticationPrincipal;
import com.connectruck.foodtruck.auth.annotation.Authorization;
import com.connectruck.foodtruck.truck.dto.TruckResponse;
import com.connectruck.foodtruck.truck.service.TruckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "owner truck", description = "푸드트럭 관련 사장님 API")
@RestController
@Authorization(OWNER)
@RequestMapping("/api/owner/trucks")
@RequiredArgsConstructor
public class OwnerTruckController {

    private final TruckService truckService;

    @Operation(summary = "사장님 계정의 소유 푸드트럭 정보 조회")
    @GetMapping("/my")
    public TruckResponse findMyTruck(@AuthenticationPrincipal final Long ownerId) {
        return truckService.findByOwnerId(ownerId);
    }
}
