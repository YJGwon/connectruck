package com.connectruck.foodtruck.order.infra;

import com.connectruck.foodtruck.order.message.OrderCreatedMessage;
import com.connectruck.foodtruck.order.message.OrderMessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RedisOrderMessagePublisher implements OrderMessagePublisher {

    private final RedisTemplate<String, OrderCreatedMessage> orderCreatedMessageTemplate;
    private final ChannelTopic orderCreatedChannelTopic;

    @Override
    public void publishCreatedMessage(final OrderCreatedMessage message) {
        orderCreatedMessageTemplate.convertAndSend(orderCreatedChannelTopic.getTopic(), message);
    }
}
