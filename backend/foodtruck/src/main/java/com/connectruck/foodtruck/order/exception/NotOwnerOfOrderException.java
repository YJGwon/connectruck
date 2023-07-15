package com.connectruck.foodtruck.order.exception;

import com.connectruck.foodtruck.common.exception.ClientException;

public class NotOwnerOfOrderException extends ClientException {

    private static final String TITLE = "소유하지 않은 푸드트럭의 주문입니다.";
    private static final String DETAIL = "소유하지 않은 푸드트럭의 주문을 처리할 수 없습니다.";

    public NotOwnerOfOrderException() {
        super(TITLE, DETAIL);
    }
}
