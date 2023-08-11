package com.connectruck.foodtruck.notification.config;

import com.connectruck.foodtruck.notification.service.NotificationService;
import com.connectruck.foodtruck.notification.service.OrderCreatedMessageSubscriber;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisMessageListenerConfig {

    @Bean
    OrderCreatedMessageSubscriber listener(final NotificationService notificationService,
                                           final ObjectMapper objectMapper) {
        return new OrderCreatedMessageSubscriber(notificationService, objectMapper);
    }

    @Bean
    MessageListenerAdapter messageListenerAdapter(final OrderCreatedMessageSubscriber listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(final RedisConnectionFactory connectionFactory,
                                                                final MessageListenerAdapter listener,
                                                                final ChannelTopic orderCreatedChannelTopic) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listener, orderCreatedChannelTopic);
        return container;
    }
}
