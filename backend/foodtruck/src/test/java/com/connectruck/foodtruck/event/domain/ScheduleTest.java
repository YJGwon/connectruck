package com.connectruck.foodtruck.event.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ScheduleTest {

    @DisplayName("일정이 특정 시간에 닫혀있는지 확인한다.")
    @ParameterizedTest(name = "{0}시 {1}분에 열고 {2}시 {3}분에 닫는 일정은 10시에 대해 {4}")
    @CsvSource(value = {
            "10, 0, 18, 0, false",
            "10, 1, 18, 0, true",
            "9, 0, 9, 59, true",
            "9, 0, 10, 0, false"})
    void isClosedAt(final int openHour, final int openMinute,
                    final int closeHour, final int closeMinute,
                    final boolean expected) {
        // given
        final LocalTime openAt = LocalTime.of(openHour, openMinute);
        final LocalTime closeAt = LocalTime.of(closeHour, closeMinute);
        final Schedule schedule = Schedule.ofNew(LocalDate.now(), openAt, closeAt, 0L);

        // when
        final LocalTime ten = LocalTime.of(10, 0);
        final boolean actual = schedule.isClosedAt(ten);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
