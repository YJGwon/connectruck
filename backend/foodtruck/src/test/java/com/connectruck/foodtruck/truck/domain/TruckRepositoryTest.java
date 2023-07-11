package com.connectruck.foodtruck.truck.domain;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static com.connectruck.foodtruck.common.fixture.data.EventFixture.서울FC_경기;
import static org.assertj.core.api.Assertions.assertThat;

import com.connectruck.foodtruck.common.testbase.RepositoryTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

class TruckRepositoryTest extends RepositoryTestBase {

    @Autowired
    private TruckRepository truckRepository;

    @DisplayName("특정 푸드트럭을 id로 조회한다.")
    @Test
    void findById() {
        // given
        final Event event = 밤도깨비_야시장.create();
        dataSetup.saveEvent(event);
        final Truck expected = dataSetup.saveTruck(event);

        // when
        final Optional<Truck> found = truckRepository.findById(expected.getId());

        // then
        assertThat(found.get()).isEqualTo(expected);
    }

    @DisplayName("행사 참가 푸드트럭 목록 조회")
    @Nested
    class findByEventId {

        @DisplayName("slice 단위로 조회한다.")
        @Test
        void perSlice() {
            // given
            final Event event = 밤도깨비_야시장.create();
            dataSetup.saveEvent(event);
            dataSetup.saveTruck(event);
            dataSetup.saveTruck(event);
            dataSetup.saveTruck(event);

            // when
            final int page = 0;
            final int size = 2;
            final Slice<Truck> found = truckRepository.findByEventId(event.getId(),
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
            dataSetup.saveTruck(event);

            final Event otherEvent = 서울FC_경기.create();
            dataSetup.saveEvent(otherEvent);
            dataSetup.saveTruck(otherEvent);

            // when
            final Slice<Truck> found = truckRepository.findByEventId(event.getId(),
                    PageRequest.of(0, 2));

            // then
            assertThat(found.getContent()).hasSize(1);
        }
    }
}
