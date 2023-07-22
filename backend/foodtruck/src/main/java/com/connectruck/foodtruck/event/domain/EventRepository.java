package com.connectruck.foodtruck.event.domain;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface EventRepository extends Repository<Event, Long> {

    Optional<Event> findById(Long id);
}
