package com.connectruck.foodtruck.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SseEventRepositoryTest {

    private final SseEventRepository sseEventRepository = new SseEventRepositoryImpl();

    @DisplayName("특정 group에서 id가 더 큰 SSE 기록을 조회한다.")
    @Test
    void findByGroupIdAndIdGraterThan() {
        // given
        final Long groupId = 0L;
        final SseEvent expected = new SseEvent("0_003", "event", "something3");
        sseEventRepository.save(groupId, new SseEvent("0_001", "event", "something1"));
        sseEventRepository.save(groupId, new SseEvent("0_002", "event", "something2"));
        sseEventRepository.save(groupId, expected);

        // when
        final List<SseEvent> found = sseEventRepository.findByGroupIdAndIdGraterThan(groupId, "0_002");

        // then
        assertThat(found).containsExactly(expected);
    }
}
