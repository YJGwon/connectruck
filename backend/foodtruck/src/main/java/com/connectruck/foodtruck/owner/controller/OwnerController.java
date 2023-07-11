package com.connectruck.foodtruck.owner.controller;

import static com.connectruck.foodtruck.user.domain.Role.OWNER;

import com.connectruck.foodtruck.auth.annotation.AuthenticationPrincipal;
import com.connectruck.foodtruck.auth.annotation.Authorization;
import com.connectruck.foodtruck.truck.dto.TruckResponse;
import com.connectruck.foodtruck.truck.service.TruckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Authorization(OWNER)
@RequestMapping("/api/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final TruckService truckService;

    @Operation(summary = "사장님 계정의 소유 푸드트럭 정보 조회")
    @ApiResponse(responseCode = "401", description = "로그인 하지 않음")
    @ApiResponse(responseCode = "403", description = "사장님 계정 아님")
    @ApiResponse(responseCode = "404", description = "해당 계정이 소유한 푸드트럭 존재하지 않음")
    @GetMapping("/trucks/my")
    public TruckResponse findMyTruck(@AuthenticationPrincipal final Long ownerId) {
        return truckService.findByOwnerId(ownerId);
    }
}
