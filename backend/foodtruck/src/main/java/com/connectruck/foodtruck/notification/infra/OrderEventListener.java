package com.connectruck.foodtruck.notification.infra;

import com.connectruck.foodtruck.order.infra.SpringOrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final OrderMessageHandler orderMessageHandler;

    @EventListener
    public void onEvent(final SpringOrderEvent springOrderEvent) {
        orderMessageHandler.handle(springOrderEvent.getOrderMessage());
    }
}
