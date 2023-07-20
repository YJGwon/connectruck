package com.connectruck.foodtruck.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SseEvent {

    private final String id;
    private final String name;
    private final String data;
}
