package com.connectruck.foodtruck.order.message;

public interface OrderMessagePublisher {

    void publish(OrderMessage message);
}
