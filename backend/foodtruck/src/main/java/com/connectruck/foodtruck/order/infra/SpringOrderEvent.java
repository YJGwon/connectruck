package com.connectruck.foodtruck.order.infra;

import com.connectruck.foodtruck.order.message.OrderMessage;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SpringOrderEvent extends ApplicationEvent {

    private final OrderMessage orderMessage;

    public SpringOrderEvent(final Object source, final OrderMessage orderMessage) {
        super(source);
        this.orderMessage = orderMessage;
    }
}
