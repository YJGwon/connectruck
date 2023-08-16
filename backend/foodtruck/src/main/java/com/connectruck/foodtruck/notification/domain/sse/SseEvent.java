package com.connectruck.foodtruck.notification.domain.sse;

import static lombok.AccessLevel.PROTECTED;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
@Getter
@EqualsAndHashCode
public class SseEvent {

    private static final String EVENT_ID_DELIMITER = "_";

    private SseEventGroup group;
    private String name;
    private String data;
    private long timestamp = System.currentTimeMillis();

    public SseEvent(final SseEventGroup group, final String name, final String data) {
        this.group = group;
        this.name = name;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public static Long getTimestampFrom(final String eventId) {
        final String rawTimestamp = eventId.substring(eventId.indexOf(EVENT_ID_DELIMITER) + 1);
        return Long.parseLong(rawTimestamp);
    }

    public String generateEventId() {
        return group.getId() + EVENT_ID_DELIMITER + timestamp;
    }
}
