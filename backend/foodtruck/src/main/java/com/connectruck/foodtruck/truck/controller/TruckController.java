package com.connectruck.foodtruck.truck.controller;

import com.connectruck.foodtruck.truck.dto.TrucksResponse;
import com.connectruck.foodtruck.truck.service.TruckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trucks")
@RequiredArgsConstructor
@Validated
public class TruckController {

    public static final String DEFAULT_PAGE = "0";
    public static final String DEFAULT_SIZE = "20";
    private final TruckService truckService;

    @Operation(summary = "푸드트럭 목록 페이지 단위 조회")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 parameter")
    @GetMapping
    public TrucksResponse findAll(
            @RequestParam(required = false, defaultValue = DEFAULT_PAGE) @PositiveOrZero final int page,
            @RequestParam(required = false, defaultValue = DEFAULT_SIZE) @Positive final int size) {
        return truckService.findAll(page, size);
    }
}
