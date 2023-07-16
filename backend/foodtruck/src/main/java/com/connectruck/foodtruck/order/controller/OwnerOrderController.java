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
    @ApiResponse(responseCode = "400", description = "접수 대기중인 주문이 아님, 소유한 푸드트럭의 주문이 아님")
    @PostMapping("/{orderId}/accept")
    public ResponseEntity<Void> acceptOrder(@AuthenticationPrincipal final Long ownerId,
                                            @PathVariable final Long orderId) {
        orderService.acceptOrder(orderId, ownerId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "주문 조리 완료 처리")
    @ApiResponse(responseCode = "400", description = "조리중인 주문이 아님, 소유한 푸드트럭의 주문이 아님")
    @PostMapping("/{orderId}/finish-cooking")
    public ResponseEntity<Void> finishCooking(@AuthenticationPrincipal final Long ownerId,
                                              @PathVariable final Long orderId) {
        orderService.finishCooking(orderId, ownerId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "주문 픽업 완료 처리")
    @ApiResponse(responseCode = "400", description = "조리 완료된 주문이 아님, 소유한 푸드트럭의 주문이 아님")
    @PostMapping("/{orderId}/complete")
    public ResponseEntity<Void> complete(@AuthenticationPrincipal final Long ownerId,
                                         @PathVariable final Long orderId) {
        orderService.complete(orderId, ownerId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "주문 취소")
    @ApiResponse(responseCode = "400", description = "진행 중인 주문이 아님, 소유한 푸드트럭의 주문이 아님")
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancel(@AuthenticationPrincipal final Long ownerId,
                                       @PathVariable final Long orderId) {
        orderService.cancel(orderId, ownerId);
        return ResponseEntity.noContent().build();
    }
}
