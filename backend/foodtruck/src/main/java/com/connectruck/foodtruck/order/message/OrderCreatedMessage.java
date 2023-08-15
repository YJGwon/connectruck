package com.connectruck.foodtruck.order.message;

public record OrderCreatedMessage(long truckId, long orderId) {
}
