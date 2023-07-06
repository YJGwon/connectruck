package com.connectruck.foodtruck.event.service;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.event.domain.Schedule;
import com.connectruck.foodtruck.event.dto.EventResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EventServiceTest extends ServiceTestBase {

    @Autowired
    private EventService eventService;

    @DisplayName("행사 정보 조회")
    @Nested
    class findById {

        @DisplayName("특정 행사의 정보를 id로 조회한다.")
        @Test
        void success() {
            // given
            final Event expected = dataSetup.saveEvent(밤도깨비_야시장.create());

            // when
            final EventResponse response = eventService.findById(expected.getId());

            // then
            assertAll(
                    () -> assertThat(response.id()).isEqualTo(expected.getId()),
                    () -> assertThat(response.name()).isEqualTo(expected.getName())
            );
        }

        @DisplayName("해당하는 행사가 존재하지 않으면 예외가 발생한다.")
        @Test
        void throwsException_whenEventNotFound() {
            // given
            final Long fakeId = 0L;

            // when & then
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> eventService.findById(fakeId))
                    .withMessageContaining("존재하지 않습니다");
        }
    }

    @DisplayName("현재 행사가 닫혀있는지 조회")
    @Nested
    class isEventClosed {

        private final LocalDate eventDate = LocalDate.of(2023, 7, 5);
        private final LocalTime openTime = LocalTime.of(10, 0);
        private final LocalTime closeTime = LocalTime.of(20, 0);
        private Event event;

        @BeforeEach
        void setSchedule() {
            event = dataSetup.saveEvent(밤도깨비_야시장.create());

            final Schedule schedule = Schedule.ofNew(eventDate, openTime, closeTime, event.getId());
            dataSetup.saveSchedule(schedule);
        }

        @DisplayName("해당 행사가 현재 진행중이면 false를 반환한다.")
        @Test
        void returnFalse_whenEventOpened() {
            // given
            final LocalDateTime dateTime = LocalDateTime.of(eventDate, openTime);

            // when
            final boolean actual = eventService.isEventClosedAt(event.getId(), dateTime);

            // then
            assertThat(actual).isFalse();
        }

        @DisplayName("해당 행사의 오늘 일정이 없으면 true를 반환한다.")
        @Test
        void returnTrue_whenNoScheduleToday() {
            // given
            final LocalDate beforeEventDate = eventDate.minusDays(1L);
            final LocalDateTime dateTime = LocalDateTime.of(beforeEventDate, openTime);

            // when
            final boolean actual = eventService.isEventClosedAt(event.getId(), dateTime);

            // then
            assertThat(actual).isTrue();
        }

        @DisplayName("해당 행사의 오늘 일정이 시작되지 않았으면 true를 반환한다.")
        @Test
        void returnTrue_whenNotOpenedYet() {
            // given
            final LocalTime beforeOpenTime = openTime.minusMinutes(1);
            final LocalDateTime dateTime = LocalDateTime.of(eventDate, beforeOpenTime);

            // when
            final boolean actual = eventService.isEventClosedAt(event.getId(), dateTime);

            // then
            assertThat(actual).isTrue();
        }

        @DisplayName("해당 행사의 오늘 일정이 종료되었면 true를 반환한다.")
        @Test
        void returnTrue_whenClosed() {
            // given
            final LocalTime afterCloseTime = closeTime.plusMinutes(1);
            final LocalDateTime dateTime = LocalDateTime.of(eventDate, afterCloseTime);

            // when
            final boolean actual = eventService.isEventClosedAt(event.getId(), dateTime);

            // then
            assertThat(actual).isTrue();
        }
    }
}
