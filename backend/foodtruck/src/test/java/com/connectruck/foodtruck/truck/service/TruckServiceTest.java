package com.connectruck.foodtruck.truck.service;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.서울FC_경기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.connectruck.foodtruck.common.dto.PageResponse;
import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.truck.domain.Participation;
import com.connectruck.foodtruck.truck.dto.ParticipationResponse;
import com.connectruck.foodtruck.truck.dto.ParticipationsResponse;
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
        final Event event = Event.ofNew("여의도 밤도깨비 야시장", "서울 영등포구 여의동 여의동로 330");
        dataSetup.saveEvent(event);
        dataSetup.saveParticipation(event);
        dataSetup.saveParticipation(event);

        // 다른 행사 참가 푸드트럭 1개 존재
        final Event otherEvent = 서울FC_경기.create();
        dataSetup.saveEvent(otherEvent);
        dataSetup.saveParticipation(otherEvent);

        // when
        final int page = 0;
        final int size = 2;
        final ParticipationsResponse response = truckService.findByEvent(event.getId(), page, size);

        // then
        assertAll(
                () -> assertThat(response.trucks()).hasSize(2),
                () -> assertThat(response.page()).isEqualTo(new PageResponse(size, page, false))
        );
    }

    @DisplayName("행사 참가 푸드트럭 정보 조회")
    @Nested
    class findByParticipationId {

        @DisplayName("특정 행사 참가 푸드트럭의 정보를 id로 조회한다.")
        @Test
        void success() {
            // given
            final Event event = Event.ofNew("여의도 밤도깨비 야시장", "서울 영등포구 여의동 여의동로 330");
            dataSetup.saveEvent(event);
            final Participation expected = dataSetup.saveParticipation(event);

            // when
            final ParticipationResponse response = truckService.findByParticipationId(expected.getId());

            // then
            assertAll(
                    () -> assertThat(response.id()).isEqualTo(expected.getId()),
                    () -> assertThat(response.name()).isEqualTo(expected.getTruck().getName())
            );
        }

        @DisplayName("해당하는 행사 참가 푸드트럭이 존재하지 않으면 예외가 발생한다.")
        @Test
        void throwsException_whenParticipationNotFound() {
            // given
            final long fakeId = 0L;

            // when & then
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> truckService.findByParticipationId(fakeId))
                    .withMessageContaining("존재하지 않습니다");
        }
    }
}
