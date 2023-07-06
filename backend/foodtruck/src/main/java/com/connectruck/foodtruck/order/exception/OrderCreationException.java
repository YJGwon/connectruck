package com.connectruck.foodtruck.order.exception;

import com.connectruck.foodtruck.common.exception.ClientException;

public class OrderCreationException extends ClientException {

    private static final String TITLE = "주문할 수 없습니다.";
    private static final String DETAIL_CLOSED = "푸드트럭 운영 시간이 아닐 때에는 주문할 수 없습니다.";
    private static final String DETAIL_OTHER_TRUCK = "다른 푸드트럭의 메뉴는 주문할 수 없습니다.";

    private OrderCreationException(final String detail) {
        super(TITLE, detail);
    }

    public static OrderCreationException ofClosed() {
        return new OrderCreationException(DETAIL_CLOSED);
    }

    public static OrderCreationException ofOtherTruck() {
        return new OrderCreationException(DETAIL_OTHER_TRUCK);
    }
}
