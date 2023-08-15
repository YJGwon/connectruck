package com.connectruck.foodtruck.order.infra;

import static org.assertj.core.api.Assertions.assertThatNoException;

import com.connectruck.foodtruck.order.domain.OrderStatus;
import com.connectruck.foodtruck.order.message.OrderMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisOrderMessagePublisherTest {

    @Autowired
    private RedisOrderMessagePublisher redisOrderMessagePublisher;

    @DisplayName("주문 생성 메세지를 발송한다.")
    @Test
    void publishCreatedMessage() {
        // given
        final OrderMessage message = new OrderMessage(0L, OrderStatus.CREATED, 0L);

        // when & then
        assertThatNoException()
                .isThrownBy(() -> redisOrderMessagePublisher.publishCreatedMessage(message));
    }
}
