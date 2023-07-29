package com.connectruck.foodtruck.notification.domain;

import static lombok.AccessLevel.PROTECTED;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
@Getter
@EqualsAndHashCode
public class SseEvent {

    private String groupType;
    private Long groupId;
    private String name;
    private String data;
    private long timestamp = System.currentTimeMillis();

    public SseEvent(final String groupType, final Long groupId, final String name, final String data) {
        this.groupType = groupType;
        this.groupId = groupId;
        this.name = name;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
}
