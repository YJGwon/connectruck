package com.connectruck.foodtruck.notification.infra;

import com.connectruck.foodtruck.order.message.OrderMessage;

public interface OrderMessageHandler {

    void handle(OrderMessage orderMessage);
}
