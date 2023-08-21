package com.connectruck.foodtruck.order.infra;

import com.connectruck.foodtruck.order.message.OrderMessage;
import com.connectruck.foodtruck.order.message.OrderMessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringOrderMessagePublisher implements OrderMessagePublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(final OrderMessage message) {
        final SpringOrderEvent event = new SpringOrderEvent(this, message);
        applicationEventPublisher.publishEvent(event);
    }
}
