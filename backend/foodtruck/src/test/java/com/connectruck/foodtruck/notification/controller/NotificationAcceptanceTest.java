package com.connectruck.foodtruck.notification.controller;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.connectruck.foodtruck.common.testbase.AcceptanceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.notification.domain.PushSubscription;
import com.connectruck.foodtruck.notification.dto.PushSubscribeRequest;
import com.connectruck.foodtruck.notification.dto.PushUnsubscribeRequest;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.Role;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NotificationAcceptanceTest extends AcceptanceTestBase {

    private static final String BASE_URI = "/api/notification";

    @DisplayName("소유한 푸드트럭의 주문 push 알림을 구독한다.")
    @Test
    void subscribeOrders() {
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

    @DisplayName("소유한 푸드트럭의 주문 push 알림을 구독을 해지한다.")
    @Test
    void unsubscribeOrders() {
        // given
        // 사장님 계정 등록, 로그인
        final String username = "test";
        final String password = "test1234!";
        final Account owner = dataSetup.saveAccount(Account.ofNew(username, password, "01000000000", Role.OWNER));
        final String token = loginAndGetToken(username, password);

        // 소유 푸드트럭 1개 저장
        final Event event = dataSetup.saveEvent(밤도깨비_야시장.create());
        final Truck truck = dataSetup.saveTruck(event, owner.getId());

        // 구독 정보 저장
        final PushSubscription pushSubscription = dataSetup.savePushSubscription("fake.token", truck);

        // given & when
        final PushUnsubscribeRequest request = new PushUnsubscribeRequest(pushSubscription.getToken());
        final ValidatableResponse response = deleteWithToken(BASE_URI + "/orders/my/subscription", request, token);

        // then
        response.statusCode(NO_CONTENT.value());
    }
}
