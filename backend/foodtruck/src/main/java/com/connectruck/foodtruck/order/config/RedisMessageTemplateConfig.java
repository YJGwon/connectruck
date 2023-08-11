package com.connectruck.foodtruck.order.config;

import com.connectruck.foodtruck.common.support.PrefixedStringRedisSerializer;
import com.connectruck.foodtruck.order.infra.OrderCreatedMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisMessageTemplateConfig {

    @Bean
    RedisTemplate<String, OrderCreatedMessage> orderCreatedMessageTemplate(
            final RedisConnectionFactory redisConnectionFactory,
            final PrefixedStringRedisSerializer prefixedStringRedisSerializer) {
        final RedisTemplate<String, OrderCreatedMessage> orderCreatedMessageTemplate = new RedisTemplate<>();
        orderCreatedMessageTemplate.setConnectionFactory(redisConnectionFactory);
        orderCreatedMessageTemplate.setKeySerializer(prefixedStringRedisSerializer);
        orderCreatedMessageTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(OrderCreatedMessage.class));
        return orderCreatedMessageTemplate;
    }
}
