package com.connectruck.foodtruck.notification.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SseEvent {

    private final Long groupId;
    private final String name;
    private final String data;
    private final long timestamp = System.currentTimeMillis();
}
