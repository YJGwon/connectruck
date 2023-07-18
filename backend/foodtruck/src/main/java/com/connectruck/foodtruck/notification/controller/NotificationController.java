package com.connectruck.foodtruck.notification.controller;

import static com.connectruck.foodtruck.user.domain.Role.OWNER;

import com.connectruck.foodtruck.auth.annotation.AuthenticationPrincipal;
import com.connectruck.foodtruck.auth.annotation.Authorization;
import com.connectruck.foodtruck.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "사장님 계정의 소유 푸드트럭 주문 알림 구독")
    @Authorization(OWNER)
    @GetMapping("/orders/my")
    public ResponseEntity<SseEmitter> subscribeOrders(@AuthenticationPrincipal final Long ownerId) {
        final SseEmitter sseEmitter = notificationService.subscribeOrders(ownerId);
        return ResponseEntity.ok()
                .header(HttpHeaders.TRANSFER_ENCODING, "chunked")
                .body(sseEmitter);
    }
}
