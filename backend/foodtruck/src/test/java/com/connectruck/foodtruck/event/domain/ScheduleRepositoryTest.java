package com.connectruck.foodtruck.event.domain;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThat;

import com.connectruck.foodtruck.common.testbase.RepositoryTestBase;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ScheduleRepositoryTest extends RepositoryTestBase {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @DisplayName("행사 id와 날짜로 일정을 조회한다.")
    @Test
    void findByEventIdAndEventDate() {
        // given
        final Event event = 밤도깨비_야시장.create();
        dataSetup.saveEvent(event);

        final LocalDate eventDate = LocalDate.of(2023, 7, 5);
        final Schedule expected = Schedule.ofNew(eventDate, LocalTime.of(18, 0), LocalTime.of(23, 0), event.getId());
        dataSetup.saveSchedule(expected);

        // when
        final Optional<Schedule> found = scheduleRepository
                .findByEventIdAndEventDate(event.getId(), eventDate);

        // then
        assertThat(found.get()).isEqualTo(expected);
    }
}
