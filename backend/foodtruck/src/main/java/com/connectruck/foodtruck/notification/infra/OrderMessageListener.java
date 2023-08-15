package com.connectruck.foodtruck.notification.infra;

import com.connectruck.foodtruck.order.message.OrderMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderMessageListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final OrderMessageHandler orderMessageHandler;

    @Override
    public void onMessage(final Message message, final byte[] pattern) {
        try {
            final OrderMessage orderMessage = objectMapper.readValue(message.getBody(), OrderMessage.class);

            orderMessageHandler.handle(orderMessage);
        } catch (IOException e) {
            log.error("failed to read order created message", e);
        }
    }
}
