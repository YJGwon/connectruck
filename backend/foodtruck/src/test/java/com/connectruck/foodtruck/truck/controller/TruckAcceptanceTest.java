package com.connectruck.foodtruck.truck.controller;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static com.connectruck.foodtruck.common.fixture.data.EventFixture.서울FC_경기;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpStatus.OK;

import com.connectruck.foodtruck.common.testbase.AcceptanceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.Role;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TruckAcceptanceTest extends AcceptanceTestBase {

    private static final String BASE_URI = "/api/trucks";

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
            dataSetup.saveTruck(event);
            dataSetup.saveTruck(event);
            final Truck expected = dataSetup.saveTruck(event);

            // 다른 행사 참가 푸드트럭 1개 존재
            final Event otherEvent = 서울FC_경기.create();
            dataSetup.saveEvent(otherEvent);
            dataSetup.saveTruck(otherEvent);

            final long eventId = event.getId();
            final int page = 1;
            final int size = 2;

            // when
            final String params = String.format("?eventId=%d&page=%d&size=%d", eventId, page, size);
            final ValidatableResponse response = get(BASE_URI + params);

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
            final ValidatableResponse response = get(BASE_URI + String.format("?eventId=%d", eventId));

            // then
            response.statusCode(OK.value())
                    .body("page.size", equalTo(20))
                    .body("page.currentPage", equalTo(0));
        }
    }

    @DisplayName("푸드트럭 정보 조회")
    @Nested
    class findOneTruck {

        @DisplayName("특정 참가 푸드트럭의 정보를 id로 조회한다.")
        @Test
        void byId() {
            // given
            final Event event = 밤도깨비_야시장.create();
            dataSetup.saveEvent(event);
            final Truck expected = dataSetup.saveTruck(event);

            // when
            final ValidatableResponse response = get(String.format(BASE_URI + "/%d", expected.getId()));

            // then
            response.statusCode(OK.value())
                    .body("id", equalTo(expected.getId().intValue()))
                    .body("name", equalTo(expected.getName()));
        }

        @DisplayName("특정 참가 푸드트럭의 정보를 사장님 id로 조회한다.")
        @Test
        void byOwnerId() {
            // given
            // 계정 생성, 로그인
            final String username = "test";
            final String password = "test1234!";
            final Account owner = dataSetup.saveAccount(Account.ofNew(username, password, "01000000000", Role.OWNER));
            final String token = loginAndGetToken(username, password);

            // 소유 푸드트럭 저장
            final Event event = 밤도깨비_야시장.create();
            dataSetup.saveEvent(event);
            final Truck expected = dataSetup.saveTruck(event, owner.getId());

            // 해당 계정의 소유 아닌 푸드트럭 1개 존재
            dataSetup.saveTruck(event);

            // when
            final ValidatableResponse response = getWithToken(BASE_URI + "/my", token);

            // then
            response.statusCode(OK.value())
                    .body("id", equalTo(expected.getId().intValue()))
                    .body("name", equalTo(expected.getName()));
        }
    }
}
