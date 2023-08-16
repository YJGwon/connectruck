package com.connectruck.foodtruck.notification.controller;

import static com.connectruck.foodtruck.user.domain.Role.OWNER;

import com.connectruck.foodtruck.auth.annotation.AuthenticationPrincipal;
import com.connectruck.foodtruck.auth.annotation.Authorization;
import com.connectruck.foodtruck.notification.dto.PushSubscribeRequest;
import com.connectruck.foodtruck.notification.service.NotificationService;
import com.connectruck.foodtruck.notification.service.PushNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "notification", description = "알림 관련 API")
@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final PushNotificationService pushNotificationService;

    @Operation(summary = "사장님 계정의 소유 푸드트럭 주문 알림 구독")
    @Authorization(OWNER)
    @GetMapping("/orders/my")
    public ResponseEntity<SseEmitter> subscribeOrders(@AuthenticationPrincipal final Long ownerId,
                                                      @RequestParam(required = false, defaultValue = "") final String lastEventId) {
        final SseEmitter sseEmitter = notificationService.subscribeOrders(ownerId, lastEventId);
        return ResponseEntity.ok()
                .header("X-Accel-Buffering", "no")
                .body(sseEmitter);
    }

    @Operation(summary = "사장님 계정의 소유 푸드트럭 주문 push 알림 구독")
    @Authorization(OWNER)
    @PostMapping("/orders/my/subscription")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void subscribeOrdersPush(@RequestBody @Valid final PushSubscribeRequest request,
                                    @AuthenticationPrincipal final Long ownerId) {
        pushNotificationService.subscribeOrders(request, ownerId);
    }
}
