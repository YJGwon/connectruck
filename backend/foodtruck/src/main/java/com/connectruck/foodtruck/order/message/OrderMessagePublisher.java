package com.connectruck.foodtruck.order.message;

public interface OrderMessagePublisher {

    void publishCreatedMessage(OrderCreatedMessage message);
}
