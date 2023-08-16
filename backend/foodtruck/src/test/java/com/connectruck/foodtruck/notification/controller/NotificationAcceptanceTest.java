package com.connectruck.foodtruck.notification.controller;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.connectruck.foodtruck.common.testbase.AcceptanceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.notification.dto.PushSubscribeRequest;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.Role;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class NotificationAcceptanceTest extends AcceptanceTestBase {

    private static final String BASE_URI = "/api/notification";

    @DisplayName("소유 푸드트럭 주문 알림 구독")
    @Nested
    class subscribeMyOrders {

        private String token;

        @BeforeEach
        void setUp() {
            // 사장님 계정 등록, 로그인
            final String username = "test";
            final String password = "test1234!";
            final Account owner = dataSetup.saveAccount(Account.ofNew(username, password, "01000000000", Role.OWNER));
            token = loginAndGetToken(username, password);

            // 소유 푸드트럭 1개 저장
            final Event event = dataSetup.saveEvent(밤도깨비_야시장.create());
            dataSetup.saveTruck(event, owner.getId());
        }

        @DisplayName("소유한 푸드트럭의 주문 알림을 구독한다.")
        @Test
        void withOutLastEventId() {
            // given & when
            final ValidatableResponse response = RestAssured.given().log().all()
                    .auth().oauth2(token)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when().get(BASE_URI + "/orders/my")
                    .then().log().headers(); // body log 할 경우 connection 끝날 때 까지 chunked body 기다리기 때문에 생략

            // then
            response.statusCode(OK.value())
                    .header(HttpHeaders.TRANSFER_ENCODING, "chunked");
        }

        @DisplayName("구독할 때, 마지막으로 수신한 이벤트 이후에 이벤트가 더 있었다면 마저 수신한다..")
        @Test
        void withLastEventId() {
            // given & when
            final ValidatableResponse response = RestAssured.given().log().all()
                    .auth().oauth2(token)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when().get(BASE_URI + "/orders/my?lastEventId=1_001")
                    .then().log().headers();

            // then
            response.statusCode(OK.value())
                    .header(HttpHeaders.TRANSFER_ENCODING, "chunked");
        }
    }

    @DisplayName("소유한 푸드트럭의 주문 push 알림을 구독한다.")
    @Test
    void withOutLastEventId() {
        // given
        // 사장님 계정 등록, 로그인
        final String username = "test";
        final String password = "test1234!";
        final Account owner = dataSetup.saveAccount(Account.ofNew(username, password, "01000000000", Role.OWNER));
        final String token = loginAndGetToken(username, password);

        // 소유 푸드트럭 1개 저장
        final Event event = dataSetup.saveEvent(밤도깨비_야시장.create());
        dataSetup.saveTruck(event, owner.getId());

        // given & when
        final PushSubscribeRequest request = new PushSubscribeRequest("fake.token");
        final ValidatableResponse response = postWithToken(BASE_URI + "/orders/my/subscription", request, token);

        // then
        response.statusCode(NO_CONTENT.value());
    }
}
