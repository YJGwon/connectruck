package com.connectruck.foodtruck.order.message;

import com.connectruck.foodtruck.order.domain.OrderInfo;
import com.connectruck.foodtruck.order.domain.OrderStatus;

public record OrderMessage(long orderId, OrderStatus status, long truckId) {

    public static OrderMessage of(final OrderInfo orderInfo) {
        return new OrderMessage(orderInfo.getId(), orderInfo.getStatus(), orderInfo.getTruckId());
    }
}
