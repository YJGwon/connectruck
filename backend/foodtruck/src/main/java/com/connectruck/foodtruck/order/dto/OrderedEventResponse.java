package com.connectruck.foodtruck.order.dto;

import com.connectruck.foodtruck.event.domain.Event;

public record OrderedEventResponse(
        Long id,
        String name,
        String location
) {

    public static OrderedEventResponse of(final Event event) {
        return new OrderedEventResponse(event.getId(), event.getName(), event.getLocation());
    }
}
