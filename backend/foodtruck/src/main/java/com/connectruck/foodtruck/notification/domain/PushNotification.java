package com.connectruck.foodtruck.notification.domain;

import com.connectruck.foodtruck.order.domain.OrderStatus;
import java.util.Map;

public record PushNotification(String title, String body, Map<String, String> data) {

    private static final Map<OrderStatus, String> ORDER_TITLES = Map.of(
            OrderStatus.CREATED, "새로운 주문 도착!",
            OrderStatus.CANCELED, "주문이 취소되었습니다."
    );
    private static final String ORDER_BODY = "주문 접수 화면에서 확인하세요.";

    public static PushNotification ofOrder(final Long orderId, final OrderStatus status) {
        return new PushNotification(
                ORDER_TITLES.get(status),
                ORDER_BODY,
                Map.of(
                        "orderId", Long.toString(orderId),
                        "status", status.name()
                )
        );
    }
}
