package com.connectruck.foodtruck.notification.domain.sse;

import java.util.List;

public interface SseEventRepository {


    void save(SseEvent sseEvent);

    List<SseEvent> findByGroupAndTimestampGraterThan(SseEventGroup group, long timestamp);
}
