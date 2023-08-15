package com.connectruck.foodtruck.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
public class RedisChannelConfig {

    @Bean
    ChannelTopic orderChannelTopic(
            @Value("${connectruck.redis.channel.order}") final String orderChannelName) {
        return ChannelTopic.of(orderChannelName);
    }
}
