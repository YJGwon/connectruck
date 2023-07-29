package com.connectruck.foodtruck.notification.domain;

import java.util.List;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RedisSseEventRepository implements SseEventRepository {

    private static final String KEY_FORMAT = "sse:%s:%d";

    private final ZSetOperations<String, SseEvent> sseEventZSetOperations;

    RedisSseEventRepository(final RedisTemplate<String, SseEvent> sseEventTemplate) {
        this.sseEventZSetOperations = sseEventTemplate.opsForZSet();
    }

    @Override
    public void save(final SseEvent sseEvent) {
        final String key;
        key = String.format(KEY_FORMAT, sseEvent.getGroupType(), sseEvent.getGroupId());
        sseEventZSetOperations.add(key, sseEvent, sseEvent.getTimestamp());
    }

    @Override
    public List<SseEvent> findByTypeAndTargetIdAndTimestampGraterThan(final String groupType, Long groupId,
                                                                      final long timestamp) {
        final String key = String.format(KEY_FORMAT, groupType, groupId);
        return sseEventZSetOperations.rangeByScore(key, timestamp + 1, Long.MAX_VALUE)
                .stream()
                .toList();
    }
}
