package com.connectruck.foodtruck.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
public class RedisChannelConfig {

    @Bean
    ChannelTopic orderCreatedChannelTopic(
            @Value("${connectruck.redis.channel.order-created}") final String orderCreatedChannelName) {
        return ChannelTopic.of(orderCreatedChannelName);
    }
}
