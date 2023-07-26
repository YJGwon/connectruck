package com.connectruck.foodtruck.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SseEventRepositoryTest {

    private final SseEventRepository sseEventRepository = new LocalSseEventRepository();

    @DisplayName("특정 group에서 timestamp 값이 더 큰 SSE 기록을 조회한다.")
    @Test
    void findByGroupIdAndTimestampGraterThan() {
        // given
        final Long groupId = 0L;

        sseEventRepository.save(new SseEvent(groupId, "event", "something1"));
        sseEventRepository.save(new SseEvent(groupId, "event", "something2"));

        final long timestamp = System.currentTimeMillis();
        final SseEvent expected = new SseEvent(groupId, "event", "something3");
        sseEventRepository.save(expected);

        // when
        final List<SseEvent> found = sseEventRepository.findByGroupIdAndTimestampGraterThan(groupId, timestamp);

        // then
        assertThat(found).containsExactly(expected);
    }
}
