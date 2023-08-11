package com.connectruck.foodtruck.notification.service;

import com.connectruck.foodtruck.order.infra.OrderCreatedMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedMessageSubscriber implements MessageListener {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(final Message message, final byte[] pattern) {
        try {
            final OrderCreatedMessage orderCreatedMessage = objectMapper.readValue(
                    message.getBody(), OrderCreatedMessage.class
            );

            notificationService.notifyOrderCreated(orderCreatedMessage.truckId(), orderCreatedMessage.orderId());
        } catch (IOException e) {
            log.error("failed to read order created message", e);
        }
    }
}
