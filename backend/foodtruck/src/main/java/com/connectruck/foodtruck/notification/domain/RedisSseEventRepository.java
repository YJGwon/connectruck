package com.connectruck.foodtruck.notification.domain;

import java.util.List;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RedisSseEventRepository implements SseEventRepository {

    private final ZSetOperations<String, SseEvent> sseEventZSetOperations;

    RedisSseEventRepository(final RedisTemplate<String, SseEvent> sseEventTemplate) {
        this.sseEventZSetOperations = sseEventTemplate.opsForZSet();
    }

    @Override
    public void save(final SseEvent sseEvent) {
        sseEventZSetOperations.add(sseEvent.getGroupId().toString(), sseEvent, sseEvent.getTimestamp());
    }

    @Override
    public List<SseEvent> findByGroupIdAndTimestampGraterThan(final Long groupId, final long timestamp) {
        return sseEventZSetOperations.rangeByScore(groupId.toString(), timestamp + 1, Long.MAX_VALUE)
                .stream()
                .toList();
    }
}
