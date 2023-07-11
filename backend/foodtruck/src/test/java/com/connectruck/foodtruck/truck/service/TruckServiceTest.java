package com.connectruck.foodtruck.truck.service;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static com.connectruck.foodtruck.common.fixture.data.EventFixture.서울FC_경기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.connectruck.foodtruck.common.dto.PageResponse;
import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.truck.dto.TruckResponse;
import com.connectruck.foodtruck.truck.dto.TrucksResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TruckServiceTest extends ServiceTestBase {

    @Autowired
    private TruckService truckService;

    @DisplayName("행사 참가 푸드트럭에 대해 페이지 단위로 조회한다.")
    @Test
    void findByEvent() {
        // given
        // 총 2개의 푸드트럭 참가
        final Event event = 밤도깨비_야시장.create();
        dataSetup.saveEvent(event);
        dataSetup.saveTruck(event);
        dataSetup.saveTruck(event);

        // 다른 행사 참가 푸드트럭 1개 존재
        final Event otherEvent = 서울FC_경기.create();
        dataSetup.saveEvent(otherEvent);
        dataSetup.saveTruck(otherEvent);

        // when
        final int page = 0;
        final int size = 2;
        final TrucksResponse response = truckService.findByEvent(event.getId(), page, size);

        // then
        assertAll(
                () -> assertThat(response.trucks()).hasSize(2),
                () -> assertThat(response.page()).isEqualTo(new PageResponse(size, 0, page, false))
        );
    }

    @DisplayName("푸드트럭 정보 조회")
    @Nested
    class findById {

        @DisplayName("특정 푸드트럭의 정보를 id로 조회한다.")
        @Test
        void success() {
            // given
            final Event event = 밤도깨비_야시장.create();
            dataSetup.saveEvent(event);
            final Truck expected = dataSetup.saveTruck(event);

            // when
            final TruckResponse response = truckService.findById(expected.getId());

            // then
            assertAll(
                    () -> assertThat(response.id()).isEqualTo(expected.getId()),
                    () -> assertThat(response.name()).isEqualTo(expected.getName())
            );
        }

        @DisplayName("해당하는 푸드트럭이 존재하지 않으면 예외가 발생한다.")
        @Test
        void throwsException_whenTruckNotFound() {
            // given
            final long fakeId = 0L;

            // when & then
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> truckService.findById(fakeId))
                    .withMessageContaining("존재하지 않습니다");
        }
    }

    @DisplayName("푸드트럭의 참가 행사 id 조회")
    @Nested
    class findEventIdById {

        @DisplayName("특정 행사 참가 푸드트럭의 행사 id를 id로 조회한다.")
        @Test
        void success() {
            // given
            final Event event = 밤도깨비_야시장.create();
            dataSetup.saveEvent(event);
            final Truck savedTruck = dataSetup.saveTruck(event);

            // when
            final Long actual = truckService.findEventIdById(savedTruck.getId());

            // then
            assertThat(actual).isEqualTo(event.getId());
        }

        @DisplayName("해당하는 푸드트럭이 존재하지 않으면 예외가 발생한다.")
        @Test
        void throwsException_whenTruckNotFound() {
            // given
            final long fakeId = 0L;

            // when & then
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> truckService.findEventIdById(fakeId))
                    .withMessageContaining("존재하지 않습니다");
        }
    }
}
