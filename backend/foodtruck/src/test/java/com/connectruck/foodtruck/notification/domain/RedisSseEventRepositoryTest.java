package com.connectruck.foodtruck.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.connectruck.foodtruck.notification.config.RedisSseEventTemplateConfig;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

@DataRedisTest
@Import({RedisSseEventTemplateConfig.class, RedisSseEventRepository.class})
class RedisSseEventRepositoryTest {

    @Autowired
    private RedisTemplate<String, SseEvent> sseEventTemplate;
    @Autowired
    private SseEventRepository sseEventRepository;

    @AfterEach
    void tearDown() {
//        final Set<String> keys = sseEventTemplate.keys("*");
//        sseEventTemplate.delete(keys);
    }

    @DisplayName("특정 group에서 timestamp 값이 더 큰 SSE 기록을 조회한다.")
    @Test
    void findByGroupIdAndTimestampGraterThan() throws InterruptedException {
        // given
        final SseEventGroup group = new SseEventGroup(SseEventGroupType.OWNER_ORDER, 0L);

        sseEventRepository.save(new SseEvent(group, "event", "something1"));
        sseEventRepository.save(new SseEvent(group, "event", "something2"));

        final long timestamp = System.currentTimeMillis();
        Thread.sleep(1);

        final SseEvent expected = new SseEvent(group, "event", "something3");
        sseEventRepository.save(expected);

        // when
        final List<SseEvent> found = sseEventRepository.findByGroupAndTimestampGraterThan(group, timestamp);

        // then
        assertThat(found).containsExactly(expected);
    }
}
