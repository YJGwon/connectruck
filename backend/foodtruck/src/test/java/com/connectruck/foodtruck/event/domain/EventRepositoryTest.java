package com.connectruck.foodtruck.event.domain;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThat;

import com.connectruck.foodtruck.common.testbase.RepositoryTestBase;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EventRepositoryTest extends RepositoryTestBase {

    @Autowired
    private EventRepository eventRepository;

    @DisplayName("아이디로 행사를 조회한다.")
    @Test
    void findById() {
        // given
        final Event expected = dataSetup.saveEvent(밤도깨비_야시장.create());

        // when
        final Optional<Event> found = eventRepository.findById(expected.getId());

        // then
        assertThat(found.get()).isEqualTo(expected);
    }
}
