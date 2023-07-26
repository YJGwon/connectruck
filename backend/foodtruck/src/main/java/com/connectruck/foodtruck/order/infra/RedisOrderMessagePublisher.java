package com.connectruck.foodtruck.order.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisOrderMessagePublisher implements OrderMessagePublisher {

    private final RedisTemplate<String, OrderCreatedMessage> orderCreatedMessageTemplate;

    @Override
    public void publishCreatedMessage(final OrderCreatedMessage message) {
        orderCreatedMessageTemplate.convertAndSend("order-created", message);
    }
}
