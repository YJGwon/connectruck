package com.connectruck.foodtruck.common.fixture;

import com.connectruck.foodtruck.event.domain.Event;
import org.springframework.data.repository.Repository;

public interface TestEventRepository extends Repository<Event, Long> {

    Event save(final Event event);
}
