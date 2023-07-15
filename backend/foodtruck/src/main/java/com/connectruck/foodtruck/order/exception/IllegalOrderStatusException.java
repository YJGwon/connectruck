package com.connectruck.foodtruck.order.exception;

import com.connectruck.foodtruck.common.exception.ClientException;
import com.connectruck.foodtruck.order.domain.OrderStatus;

public class IllegalOrderStatusException extends ClientException {

    private static final String TITLE = "주문 상태가 올바르지 않습니다.";
    private static final String DETAIL_UNACCEPTABLE = "%s 상태의 주문을 접수할 수 없습니다.";

    private IllegalOrderStatusException(final String detail) {
        super(TITLE, detail);
    }

    public static IllegalOrderStatusException ofUnacceptable(final OrderStatus orderStatus) {
        return new IllegalOrderStatusException(String.format(DETAIL_UNACCEPTABLE, orderStatus.toKorean()));
    }
}
