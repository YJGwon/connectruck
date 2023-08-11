package com.connectruck.foodtruck.order.exception;

import com.connectruck.foodtruck.common.exception.ClientException;

public class OrderCreationException extends ClientException {

    private static final String TITLE = "주문할 수 없습니다.";

    public OrderCreationException(final String detail) {
        super(TITLE, detail);
    }
}
