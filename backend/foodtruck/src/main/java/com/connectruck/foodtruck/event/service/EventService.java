package com.connectruck.foodtruck.event.service;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.event.domain.EventRepository;
import com.connectruck.foodtruck.event.domain.Schedule;
import com.connectruck.foodtruck.event.domain.ScheduleRepository;
import com.connectruck.foodtruck.event.dto.EventResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ScheduleRepository scheduleRepository;

    public EventResponse findById(final Long id) {
        final Event found = eventRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("행사", id));

        return EventResponse.of(found);
    }

    public boolean isEventClosedAt(final Long id, final LocalDateTime dateTime) {
        final Optional<Schedule> found = scheduleRepository.findByEventIdAndEventDate(id, dateTime.toLocalDate());
        if (found.isEmpty()) {
            return true;
        }

        final Schedule schedule = found.get();
        return schedule.isClosedAt(dateTime.toLocalTime());
    }
}
