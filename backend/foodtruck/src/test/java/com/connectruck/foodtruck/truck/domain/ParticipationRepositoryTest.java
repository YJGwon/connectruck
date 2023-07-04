package com.connectruck.foodtruck.truck.domain;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static com.connectruck.foodtruck.common.fixture.data.EventFixture.서울FC_경기;
import static org.assertj.core.api.Assertions.assertThat;

import com.connectruck.foodtruck.common.testbase.RepositoryTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

class ParticipationRepositoryTest extends RepositoryTestBase {

    @Autowired
    private ParticipationRepository participationRepository;

    @DisplayName("행사 참가 푸드트럭 목록 조회")
    @Nested
    class findByEventId {

        @DisplayName("slice 단위로 조회한다.")
        @Test
        void perSlice() {
            // given
            final Event event = 밤도깨비_야시장.create();
            dataSetup.saveEvent(event);
            dataSetup.saveParticipation(event);
            dataSetup.saveParticipation(event);
            dataSetup.saveParticipation(event);

            // when
            final int page = 0;
            final int size = 2;
            final Slice<Participation> found = participationRepository.findByEventId(event.getId(),
                    PageRequest.of(page, size));

            // then
            assertThat(found.getContent()).hasSize(size);
        }

        @DisplayName("특정 행사의 푸드트럭만 조회한다.")
        @Test
        void notContainingOtherEvent() {
            // given
            final Event event = 밤도깨비_야시장.create();
            dataSetup.saveEvent(event);
            dataSetup.saveParticipation(event);

            final Event otherEvent = 서울FC_경기.create();
            dataSetup.saveEvent(otherEvent);
            dataSetup.saveParticipation(otherEvent);

            // when
            final Slice<Participation> found = participationRepository.findByEventId(event.getId(),
                    PageRequest.of(0, 2));

            // then
            assertThat(found.getContent()).hasSize(1);
        }
    }
}
