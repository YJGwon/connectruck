package com.connectruck.foodtruck.order.domain;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.connectruck.foodtruck.common.exception.ClientException;
import com.connectruck.foodtruck.order.exception.IllegalOrderStatusException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class OrderInfoTest {

    @DisplayName("주문 메뉴 내역을 빈 값으로 변경하면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void changeOrderLine(final List<OrderLine> emptyLines) {
        // given
        final OrderInfo orderInfo = OrderInfo.ofNew(0L, "01000000000");

        // when & then
        assertThatExceptionOfType(ClientException.class)
                .isThrownBy(() -> orderInfo.changeOrderLine(emptyLines))
                .withMessageContaining("빈 값");
    }

    @DisplayName("접수 대기 상태가 아닌 주문을 접수하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "COOKED", "COMPLETE", "CANCELED"})
    void accept(final String unacceptableStatus) {
        // given
        final OrderInfo unacceptableOrder = new OrderInfo(
                null, 0L, "01000000000",
                OrderStatus.valueOf(unacceptableStatus),
                Collections.emptyList(), LocalDateTime.now(), LocalDateTime.now()
        );

        // when & then
        assertThatExceptionOfType(IllegalOrderStatusException.class)
                .isThrownBy(unacceptableOrder::accept)
                .withMessageContaining("접수할 수 없습니다");
    }
}
