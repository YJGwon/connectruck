package com.connectruck.foodtruck.order.exception;

import com.connectruck.foodtruck.common.exception.ClientException;

public class EventClosedException extends ClientException {

    private static final String TITLE = "현재는 주문할 수 없습니다.";
    private static final String DETAIL = "푸드트럭 운영 시간이 아닐 때에는 주문할 수 없습니다.";

    public EventClosedException() {
        super(TITLE, DETAIL);
    }
}
