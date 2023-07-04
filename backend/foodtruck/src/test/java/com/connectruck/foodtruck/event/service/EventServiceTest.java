package com.connectruck.foodtruck.event.service;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.event.dto.EventResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EventServiceTest extends ServiceTestBase {

    @Autowired
    private EventService eventService;

    @DisplayName("행사를 id로 조회한다.")
    @Nested
    class findById {

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
}
