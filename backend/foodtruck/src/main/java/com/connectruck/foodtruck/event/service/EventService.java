package com.connectruck.foodtruck.event.service;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.event.domain.EventRepository;
import com.connectruck.foodtruck.event.dto.EventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public EventResponse findById(final Long id) {
        final Event found = eventRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("행사", "eventId", id));

        return EventResponse.of(found);
    }
}
