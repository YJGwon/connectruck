package com.connectruck.foodtruck.notification.domain;

import java.util.List;

public interface SseEventRepository {


    void save(SseEvent sseEvent);

    List<SseEvent> findByGroupIdAndTimestampGraterThan(Long groupId, long timestamp);
}
