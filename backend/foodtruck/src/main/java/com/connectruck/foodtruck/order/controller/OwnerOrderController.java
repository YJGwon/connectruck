package com.connectruck.foodtruck.order.controller;

import static com.connectruck.foodtruck.common.validation.ValidationMessage.SMALLER_THAN_MIN_VALUE;
import static com.connectruck.foodtruck.user.domain.Role.OWNER;

import com.connectruck.foodtruck.auth.annotation.AuthenticationPrincipal;
import com.connectruck.foodtruck.auth.annotation.Authorization;
import com.connectruck.foodtruck.order.dto.OrdersResponse;
import com.connectruck.foodtruck.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Authorization(OWNER)
@RequestMapping("/api/owner/orders")
@RequiredArgsConstructor
@Validated
public class OwnerOrderController {

    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "20";

    private static final String PAGE_MIN_VALUE_MESSAGE = SMALLER_THAN_MIN_VALUE + " : 페이지 번호, 최소값 0";
    private static final String SIZE_MIN_VALUE_MESSAGE = SMALLER_THAN_MIN_VALUE + " : 사이즈, 최소값 1";

    private final OrderService orderService;

    @Operation(summary = "사장님 계정의 소유 푸드트럭 주문 목록 조회")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 parameter")
    @ApiResponse(responseCode = "401", description = "로그인 하지 않음")
    @ApiResponse(responseCode = "403", description = "사장님 계정 아님")
    @ApiResponse(responseCode = "404", description = "해당 계정이 소유한 푸드트럭 존재하지 않음")
    @GetMapping("/my")
    public OrdersResponse findMyOrders(@AuthenticationPrincipal final Long ownerId,
                                       @RequestParam(required = false, defaultValue = "ALL") final String status,
                                       @RequestParam(required = false, defaultValue = DEFAULT_PAGE)
                                       @PositiveOrZero(message = PAGE_MIN_VALUE_MESSAGE) final int page,
                                       @RequestParam(required = false, defaultValue = DEFAULT_SIZE)
                                       @Positive(message = SIZE_MIN_VALUE_MESSAGE) final int size) {
        return orderService.findOrdersByOwnerIdAndStatus(ownerId, status, page, size);
    }

    @Operation(summary = "주문 접수 처리")
    @ApiResponse(responseCode = "400", description = "접수 대기중인 주문이 아님")
    @ApiResponse(responseCode = "401", description = "로그인 하지 않음")
    @ApiResponse(responseCode = "403", description = "사장님 계정 아님, 해당 주문이 해당 계정 소유 푸드트럭 대상이 아님")
    @ApiResponse(responseCode = "404", description = "해당하는 주문 존재하지 않음")
    @PostMapping("/{orderId}/accept")
    public ResponseEntity<Void> acceptOrder(@AuthenticationPrincipal final Long ownerId,
                                            @PathVariable final Long orderId) {
        orderService.acceptOrder(orderId, ownerId);
        return ResponseEntity.noContent().build();
    }
}
