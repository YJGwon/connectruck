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
import com.connectruck.foodtruck.user.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TruckServiceTest extends ServiceTestBase {

    @Autowired
    private TruckService truckService;

    private Event event;

    @BeforeEach
    void setUp() {
        event = dataSetup.saveEvent(밤도깨비_야시장.create());
    }

    @DisplayName("행사 참가 푸드트럭에 대해 페이지 단위로 조회한다.")
    @Test
    void findByEvent() {
        // given
        // 총 2개의 푸드트럭 참가
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

    @DisplayName("사장님 소유 푸드트럭 정보 조회")
    @Nested
    class findByOwnerId {

        @DisplayName("특정 푸드트럭의 정보를 사장님 계정 id로 조회한다.")
        @Test
        void success() {
            // given
            final Account owner = dataSetup.saveOwnerAccount();
            final Truck owningTruck = dataSetup.saveTruck(event, owner.getId());

            // 해당 계정의 소유 아닌 푸드트럭 1개 존재
            dataSetup.saveTruck(event);

            // when
            final TruckResponse response = truckService.findByOwnerId(owner.getId());

            // then
            assertAll(
                    () -> assertThat(response.id()).isEqualTo(owningTruck.getId()),
                    () -> assertThat(response.name()).isEqualTo(owningTruck.getName())
            );
        }

        @DisplayName("해당하는 푸드트럭이 존재하지 않으면 예외가 발생한다.")
        @Test
        void throwsException_whenTruckNotFound() {
            // given
            final Account ownerNotHavingTruck = dataSetup.saveOwnerAccount();

            // when & then
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> truckService.findByOwnerId(ownerNotHavingTruck.getId()))
                    .withMessageContainingAll("푸드트럭", "존재하지 않습니다");
        }
    }
}
