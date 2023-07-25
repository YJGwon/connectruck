package com.connectruck.foodtruck.order.infra;

public record OrderCreatedMessage(long truckId, long orderId) {
}
