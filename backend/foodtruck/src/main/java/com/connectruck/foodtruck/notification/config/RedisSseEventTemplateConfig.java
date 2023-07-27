package com.connectruck.foodtruck.notification.config;

import com.connectruck.foodtruck.notification.domain.SseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisSseEventTemplateConfig {

    @Bean
    RedisTemplate<String, SseEvent> sseEventTemplate(final RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, SseEvent> sseEventTemplate = new RedisTemplate<>();
        sseEventTemplate.setConnectionFactory(redisConnectionFactory);
        sseEventTemplate.setKeySerializer(new StringRedisSerializer());
        sseEventTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(SseEvent.class));
        return sseEventTemplate;
    }
}
