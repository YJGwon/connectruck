package com.connectruck.foodtruck.notification.domain;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Component;

@Component
public class LocalSseEventRepository implements SseEventRepository {

    private final ConcurrentHashMap<Long, CopyOnWriteArrayList<SseEvent>> logs = new ConcurrentHashMap<>();

    @Override
    public void save(final SseEvent sseEvent) {
        final Long groupId = sseEvent.getGroupId();
        logs.putIfAbsent(groupId, new CopyOnWriteArrayList<>());
        logs.get(groupId).add(sseEvent);
    }

    @Override
    public List<SseEvent> findByGroupIdAndTimestampGraterThan(final Long groupId, final long timestamp) {
        return logs.getOrDefault(groupId, new CopyOnWriteArrayList<>())
                .stream()
                .filter(sseEvent -> sseEvent.getTimestamp() > timestamp)
                .toList();
    }
}
