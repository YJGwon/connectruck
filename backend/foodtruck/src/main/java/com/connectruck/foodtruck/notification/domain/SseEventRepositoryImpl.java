package com.connectruck.foodtruck.notification.domain;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Component;

@Component
public class SseEventRepositoryImpl implements SseEventRepository {

    private final ConcurrentHashMap<Long, CopyOnWriteArrayList<SseEvent>> logs = new ConcurrentHashMap<>();

    @Override
    public void save(final Long groupId, final SseEvent sseEvent) {
        logs.putIfAbsent(groupId, new CopyOnWriteArrayList<>());
        logs.get(groupId).add(sseEvent);
    }

    @Override
    public List<SseEvent> findByGroupIdAndIdGraterThan(final Long groupId, final String id) {
        return logs.getOrDefault(groupId, new CopyOnWriteArrayList<>())
                .stream()
                .filter(sseEvent -> sseEvent.getId().compareTo(id) > 0)
                .toList();
    }
}
