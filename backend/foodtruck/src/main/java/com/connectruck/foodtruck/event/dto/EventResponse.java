package com.connectruck.foodtruck.event.dto;

import com.connectruck.foodtruck.event.domain.Event;

public record EventResponse(
        Long id,
        String name,
        String location
) {

    public static EventResponse of(final Event event) {
        return new EventResponse(event.getId(), event.getName(), event.getLocation());
    }
}
