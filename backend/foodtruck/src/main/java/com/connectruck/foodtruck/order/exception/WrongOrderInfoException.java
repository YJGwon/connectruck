package com.connectruck.foodtruck.order.exception;

import com.connectruck.foodtruck.common.exception.ClientException;

public class WrongOrderInfoException extends ClientException {


    private static final String TITLE = "주문 정보를 조회할 수 없습니다.";
    private static final String DETAIL = "잘못된 주문 정보 입니다.";

    public WrongOrderInfoException() {
        super(TITLE, DETAIL);
    }
}
