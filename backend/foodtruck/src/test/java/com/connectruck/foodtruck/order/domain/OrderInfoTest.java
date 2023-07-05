package com.connectruck.foodtruck.order.domain;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.connectruck.foodtruck.common.exception.ClientException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class OrderInfoTest {

    @DisplayName("주문 메뉴 내역을 빈 값으로 변경하면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void changeOrderLine(final List<OrderLine> emptyLines) {
        // given
        final OrderInfo orderInfo = OrderInfo.ofNew("01000000000");

        // when & then
        assertThatExceptionOfType(ClientException.class)
                .isThrownBy(() -> orderInfo.changeOrderLine(emptyLines))
                .withMessageContaining("빈 값");
    }
}
