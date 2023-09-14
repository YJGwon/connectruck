package com.connectruck.foodtruck.order.infra;

import com.connectruck.foodtruck.order.message.OrderMessage;
import com.connectruck.foodtruck.order.message.OrderMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SpringOrderMessagePublisher implements OrderMessagePublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Async
    @Override
    public void publish(final OrderMessage message) {
        try {
            applicationEventPublisher.publishEvent(message);
        } catch (Exception e) {
            log.error("failed to send order created message", e);
        }
    }
}
