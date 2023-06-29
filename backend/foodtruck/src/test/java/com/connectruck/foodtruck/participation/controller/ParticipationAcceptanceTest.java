package com.connectruck.foodtruck.participation.controller;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.서울FC_경기;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpStatus.OK;

import com.connectruck.foodtruck.common.testbase.AcceptanceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.truck.domain.Truck;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ParticipationAcceptanceTest extends AcceptanceTestBase {

    private static final String BASE_URI_FORMAT = "/api/events/%d/trucks";

    @DisplayName("행사 참가 푸드트럭 목록 조회")
    @Nested
    class findParticipatingTrucksByEvent {

        @DisplayName("특정 페이지를 조회한다.")
        @Test
        void perPage() {
            // given
            // 총 3개의 푸드트럭 참가
            final Event event = Event.ofNew("여의도 밤도깨비 야시장", "서울 영등포구 여의동 여의동로 330");
            dataSetup.saveEvent(event);
            dataSetup.saveParticipation(event);
            dataSetup.saveParticipation(event);
            final Truck expected = dataSetup.saveParticipation(event).getTruck();

            // 다른 행사 참가 푸드트럭 1개 존재
            final Event otherEvent = 서울FC_경기.create();
            dataSetup.saveEvent(otherEvent);
            dataSetup.saveParticipation(otherEvent);

            final long eventId = event.getId();
            final int page = 1;
            final int size = 2;

            // when
            final String uri = String.format(BASE_URI_FORMAT, eventId);
            final String params = String.format("?page=%d&size=%d", page, size);
            final ValidatableResponse response = get(uri + params);

            // then
            response.statusCode(OK.value())
                    .body("page.size", equalTo(2))
                    .body("page.currentPage", equalTo(1))
                    .body("page.hasNext", equalTo(false))
                    .body("trucks", hasSize(1))
                    .body("trucks.id", contains(expected.getId().intValue()))
                    .body("trucks.name", contains(expected.getName()));
        }

        @DisplayName("사이즈와 페이지를 지정하지 않으면 첫 20개를 조회한다.")
        @Test
        void findFirst20_withNoPageAndSize() {
            // given
            final long eventId = 1L;

            // when
            final ValidatableResponse response = get(String.format(BASE_URI_FORMAT, eventId));

            // then
            response.statusCode(OK.value())
                    .body("page.size", equalTo(20))
                    .body("page.currentPage", equalTo(0));
        }
    }
}
