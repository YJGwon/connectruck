package com.connectruck.foodtruck.truck.controller;

import static com.connectruck.foodtruck.common.validation.ValidationMessage.SMALLER_THAN_MIN_VALUE;
import static com.connectruck.foodtruck.user.domain.Role.OWNER;

import com.connectruck.foodtruck.auth.annotation.AuthenticationPrincipal;
import com.connectruck.foodtruck.auth.annotation.Authorization;
import com.connectruck.foodtruck.truck.dto.TruckResponse;
import com.connectruck.foodtruck.truck.dto.TrucksResponse;
import com.connectruck.foodtruck.truck.service.TruckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trucks")
@RequiredArgsConstructor
@Validated
public class TruckController {

    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "20";

    private static final String PAGE_MIN_VALUE_MESSAGE = SMALLER_THAN_MIN_VALUE + " : 페이지 번호, 최소값 0";
    private static final String SIZE_MIN_VALUE_MESSAGE = SMALLER_THAN_MIN_VALUE + " : 사이즈, 최소값 1";

    private final TruckService truckService;

    @Operation(summary = "행사 참가 푸드트럭 목록 페이지 단위 조회")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 parameter")
    @GetMapping
    public TrucksResponse findByEvent(
            @RequestParam final Long eventId,
            @RequestParam(required = false, defaultValue = DEFAULT_PAGE)
            @PositiveOrZero(message = PAGE_MIN_VALUE_MESSAGE) final int page,
            @RequestParam(required = false, defaultValue = DEFAULT_SIZE)
            @Positive(message = SIZE_MIN_VALUE_MESSAGE) final int size) {
        return truckService.findByEvent(eventId, page, size);
    }

    @Operation(summary = "푸드트럭 정보 조회")
    @ApiResponse(responseCode = "404", description = "존재하지 않는 푸드트럭 id")
    @GetMapping("/{truckId}")
    public TruckResponse findById(@PathVariable final Long truckId) {
        return truckService.findById(truckId);
    }

    @Operation(summary = "사장님 계정의 소유 푸드트럭 정보 조회")
    @ApiResponse(responseCode = "401", description = "로그인 하지 않음")
    @ApiResponse(responseCode = "403", description = "사장님 계정 아님")
    @ApiResponse(responseCode = "404", description = "해당 계정이 소유한 푸드트럭 존재하지 않음")
    @Authorization(OWNER)
    @GetMapping("/my")
    public TruckResponse findByOwnerId(@AuthenticationPrincipal final Long ownerId) {
        return truckService.findByOwnerId(ownerId);
    }
}
