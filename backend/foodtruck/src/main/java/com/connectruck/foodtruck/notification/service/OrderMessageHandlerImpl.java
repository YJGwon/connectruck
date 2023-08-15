package com.connectruck.foodtruck.notification.service;

import com.connectruck.foodtruck.notification.infra.OrderMessageHandler;
import com.connectruck.foodtruck.order.message.OrderMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMessageHandlerImpl implements OrderMessageHandler {

    private final NotificationService notificationService;

    @Override
    public void handle(final OrderMessage orderMessage) {
        notificationService.notifyOrderToOwner(orderMessage);
    }
}
