package com.connectruck.foodtruck.order.infra;

import com.connectruck.foodtruck.order.message.OrderMessage;
import com.connectruck.foodtruck.order.message.OrderMessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RedisOrderMessagePublisher implements OrderMessagePublisher {

    private final RedisTemplate<String, OrderMessage> orderCreatedMessageTemplate;
    private final ChannelTopic orderChannelTopic;

    @Override
    public void publishCreatedMessage(final OrderMessage message) {
        orderCreatedMessageTemplate.convertAndSend(orderChannelTopic.getTopic(), message);
    }
}
