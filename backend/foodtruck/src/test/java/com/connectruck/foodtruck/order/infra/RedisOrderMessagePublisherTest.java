package com.connectruck.foodtruck.order.infra;

import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;

@DataRedisTest
@Import(RedisOrderMessagePublisher.class)
class RedisOrderMessagePublisherTest {

    @Autowired
    private RedisOrderMessagePublisher redisOrderMessagePublisher;

    @DisplayName("주문 생성 메세지를 발송한다.")
    @Test
    void publishCreatedMessage() {
        // given
        final OrderCreatedMessage message = new OrderCreatedMessage(0L, 0L);

        // when & then
        assertThatNoException()
                .isThrownBy(() -> redisOrderMessagePublisher.publishCreatedMessage(message));
    }
}
