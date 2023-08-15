package com.connectruck.foodtruck.order.config;

import com.connectruck.foodtruck.common.support.PrefixedStringRedisSerializer;
import com.connectruck.foodtruck.order.message.OrderMessage;
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
    RedisTemplate<String, OrderMessage> orderMessageTemplate(final RedisConnectionFactory redisConnectionFactory,
                                                             final PrefixedStringRedisSerializer prefixedStringRedisSerializer) {
        final RedisTemplate<String, OrderMessage> orderMessageTemplate = new RedisTemplate<>();
        orderMessageTemplate.setConnectionFactory(redisConnectionFactory);
        orderMessageTemplate.setKeySerializer(prefixedStringRedisSerializer);
        orderMessageTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(OrderMessage.class));
        return orderMessageTemplate;
    }
}
