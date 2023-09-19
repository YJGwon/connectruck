package com.connectruck.foodtruck.order.domain;

import static com.connectruck.foodtruck.order.domain.OrderStatus.CANCELED;
import static com.connectruck.foodtruck.order.domain.OrderStatus.COMPLETE;
import static com.connectruck.foodtruck.order.domain.OrderStatus.COOKED;
import static com.connectruck.foodtruck.order.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.connectruck.foodtruck.common.exception.ClientException;
import com.connectruck.foodtruck.order.exception.IllegalOrderStatusException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @DisplayName("주문 상태 변경")
    @Nested
    class changeStatus {

        @DisplayName("할 때, 접수 대기 상태가 아닌 주문을 조리중 상태로 변경하면 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "COOKED", "COMPLETE", "CANCELED"})
        void toCooking(final String unacceptableStatus) {
            // given
            final OrderInfo unacceptableOrder = new OrderInfo(
                    null, 0L, "01000000000",
                    OrderStatus.valueOf(unacceptableStatus),
                    Collections.emptyList(), LocalDateTime.now(), LocalDateTime.now()
            );

            // when & then
            assertThatExceptionOfType(IllegalOrderStatusException.class)
                    .isThrownBy(() -> unacceptableOrder.changeStatus(COOKING))
                    .withMessageContaining("변경할 수 없습니다");
        }

        @DisplayName("할 때, 조리중 상태가 아닌 주문을 조리 완료 상태로 변경하면 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"CREATED", "COOKED", "COMPLETE", "CANCELED"})
        void toCooked(final String notCookingStatus) {
            // given
            final OrderInfo notCookingOrder = new OrderInfo(
                    null, 0L, "01000000000",
                    OrderStatus.valueOf(notCookingStatus),
                    Collections.emptyList(), LocalDateTime.now(), LocalDateTime.now()
            );

            // when & then
            assertThatExceptionOfType(IllegalOrderStatusException.class)
                    .isThrownBy(() -> notCookingOrder.changeStatus(COOKED))
                    .withMessageContaining("변경할 수 없습니다");
        }

        @DisplayName("할 때, 조리 완료 상태가 아닌 주문을 픽업 완료 상태로 변경하면 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"CREATED", "COOKING", "COMPLETE", "CANCELED"})
        void toComplete(final String notCookedStatus) {
            // given
            final OrderInfo notCookedOrder = new OrderInfo(
                    null, 0L, "01000000000",
                    OrderStatus.valueOf(notCookedStatus),
                    Collections.emptyList(), LocalDateTime.now(), LocalDateTime.now()
            );

            // when & then
            assertThatExceptionOfType(IllegalOrderStatusException.class)
                    .isThrownBy(() -> notCookedOrder.changeStatus(COMPLETE))
                    .withMessageContaining("변경할 수 없습니다");
        }

        @DisplayName("할 때, 진행중이 아닌 주문을 취소 상태로 변경하면 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"COMPLETE", "CANCELED"})
        void toCanceled(final String notInProgressStatus) {
            // given
            final OrderInfo notInProgressOrder = new OrderInfo(
                    null, 0L, "01000000000",
                    OrderStatus.valueOf(notInProgressStatus),
                    Collections.emptyList(), LocalDateTime.now(), LocalDateTime.now()
            );

            // when & then
            assertThatExceptionOfType(IllegalOrderStatusException.class)
                    .isThrownBy(() -> notInProgressOrder.changeStatus(CANCELED))
                    .withMessageContaining("변경할 수 없습니다");
        }
    }
}
