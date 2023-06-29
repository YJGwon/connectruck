package com.connectruck.foodtruck.event.service;

import static com.connectruck.foodtruck.event.fixture.EventFixture.서울FC_경기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.connectruck.foodtruck.common.dto.PageResponse;
import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.event.dto.ParticipationsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EventServiceTest extends ServiceTestBase {

    @Autowired
    private EventService eventService;

    @DisplayName("행사 참가 푸드트럭에 대해 페이지 단위로 조회한다.")
    @Test
    void findTrucks() {
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
        final ParticipationsResponse response = eventService.findTrucks(event.getId(), page, size);

        // then
        assertAll(
                () -> assertThat(response.trucks()).hasSize(2),
                () -> assertThat(response.page()).isEqualTo(new PageResponse(size, page, false))
        );
    }
}
