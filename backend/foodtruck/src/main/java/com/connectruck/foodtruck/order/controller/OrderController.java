package com.connectruck.foodtruck.order.controller;

import com.connectruck.foodtruck.order.dto.OrderRequest;
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

    @Operation(summary = "행사 참가 푸드트럭 메뉴 주문")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid final OrderRequest request) {
        final URI location = URI.create(String.format("/api/orders/%d", 1L));
        return ResponseEntity.created(location).build();
    }
}
