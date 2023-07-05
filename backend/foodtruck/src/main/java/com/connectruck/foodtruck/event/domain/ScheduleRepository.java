package com.connectruck.foodtruck.event.domain;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface ScheduleRepository extends Repository<Schedule, Long> {

    Optional<Schedule> findByEventIdAndEventDate(final Long eventId, final LocalDate eventDate);
}
