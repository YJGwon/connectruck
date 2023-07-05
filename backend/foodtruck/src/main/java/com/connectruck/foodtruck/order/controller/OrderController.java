package com.connectruck.foodtruck.order.controller;

import com.connectruck.foodtruck.order.dto.OrderRequest;
import com.connectruck.foodtruck.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "행사 참가 푸드트럭 메뉴 주문")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid final OrderRequest request) {
        final Long id = orderService.create(request);
        final URI location = URI.create(String.format("/api/orders/%d", id));
        return ResponseEntity.created(location).build();
    }
}
