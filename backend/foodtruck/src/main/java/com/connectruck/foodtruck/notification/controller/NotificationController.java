package com.connectruck.foodtruck.notification.controller;

import static com.connectruck.foodtruck.user.domain.Role.OWNER;

import com.connectruck.foodtruck.auth.annotation.AuthenticationPrincipal;
import com.connectruck.foodtruck.auth.annotation.Authorization;
import com.connectruck.foodtruck.notification.dto.PushSubscribeRequest;
import com.connectruck.foodtruck.notification.dto.PushUnsubscribeRequest;
import com.connectruck.foodtruck.notification.service.PushNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "notification", description = "알림 관련 API")
@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final PushNotificationService pushNotificationService;

    @Operation(summary = "사장님 계정의 소유 푸드트럭 주문 push 알림 구독")
    @Authorization(OWNER)
    @PostMapping("/orders/my/subscription")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void subscribeOrders(@RequestBody @Valid final PushSubscribeRequest request,
                                @AuthenticationPrincipal final Long ownerId) {
        pushNotificationService.subscribeOrders(request, ownerId);
    }

    @Operation(summary = "사장님 계정의 소유 푸드트럭 주문 push 알림 구독 해지")
    @Authorization(OWNER)
    @DeleteMapping("/orders/my/subscription")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsubscribeOrders(@RequestBody @Valid final PushUnsubscribeRequest request,
                                  @AuthenticationPrincipal final Long ownerId) {
        pushNotificationService.unsubscribeOrders(request, ownerId);
    }
}
