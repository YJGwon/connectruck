package com.connectruck.foodtruck.notification.infra;

import com.connectruck.foodtruck.order.message.OrderMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final OrderMessageHandler orderMessageHandler;

    @EventListener
    public void onEvent(final OrderMessage orderMessage) {
        orderMessageHandler.handle(orderMessage);
    }
}
