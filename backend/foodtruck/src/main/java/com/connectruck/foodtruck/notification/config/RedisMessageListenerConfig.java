package com.connectruck.foodtruck.notification.config;

import com.connectruck.foodtruck.notification.infra.OrderMessageListener;
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
    MessageListenerAdapter messageListenerAdapter(final OrderMessageListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(final RedisConnectionFactory connectionFactory,
                                                                final MessageListenerAdapter listener,
                                                                final ChannelTopic orderChannelTopic) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listener, orderChannelTopic);
        return container;
    }
}
