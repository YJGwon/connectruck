package com.connectruck.foodtruck.notification.domain;

import java.util.List;

public interface SseEventRepository {


    void save(Long groupId, SseEvent sseEvent);

    List<SseEvent> findByGroupIdAndIdGraterThan(Long groupId, String id);
}
