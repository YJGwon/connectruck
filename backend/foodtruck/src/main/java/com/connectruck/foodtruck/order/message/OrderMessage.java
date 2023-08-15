package com.connectruck.foodtruck.order.message;

import com.connectruck.foodtruck.order.domain.OrderStatus;

public record OrderMessage(long orderId, OrderStatus status, long truckId) {
}
