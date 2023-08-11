package com.connectruck.foodtruck.common.fixture.repository;

import com.connectruck.foodtruck.event.domain.Schedule;
import org.springframework.data.repository.Repository;

public interface TestScheduleRepository extends Repository<Schedule, Long> {

    Schedule save(final Schedule schedule);
}
