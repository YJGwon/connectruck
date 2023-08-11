package com.connectruck.foodtruck.order.infra;

public interface OrderMessagePublisher {

    void publishCreatedMessage(OrderCreatedMessage message);
}
