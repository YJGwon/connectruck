package com.connectruck.foodtruck.menu.controller;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.connectruck.foodtruck.common.testbase.AcceptanceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.menu.dto.MenuDescriptionRequest;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.Role;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OwnerMenuAcceptanceTest extends AcceptanceTestBase {

    private static final String BASE_URI = "/api/owner/menus";

    private String token;
    private Truck owningTruck;

    @BeforeEach
    void setUp() {
        // 사장님 계정 생성, 로그인
        final String username = "test";
        final String password = "test1234!";
        final Account owner = dataSetup.saveAccount(Account.ofNew(username, password, "01000000000", Role.OWNER));
        token = loginAndGetToken(username, password);

        // 소유 푸드트럭 1개 저장
        final Event event = dataSetup.saveEvent(밤도깨비_야시장.create());
        owningTruck = dataSetup.saveTruck(event, owner.getId());
    }

    @DisplayName("소유 푸드트럭의 메뉴 목록을 조회한다.")
    @Test
    void findMyMenus() {
        // given
        final Menu expected = dataSetup.saveMenu(owningTruck);

        // when
        final ValidatableResponse response = getWithToken(BASE_URI + "/my", token);

        // then
        response.statusCode(OK.value())
                .body("menus", hasSize(1))
                .body("menus.id", contains(expected.getId().intValue()))
                .body("menus.name", contains(expected.getName()));
    }

    @DisplayName("소유 푸드트럭의 메뉴 설명을 변경한다.")
    @Test
    void updateDescription() {
        // given
        final Menu savedMenu = dataSetup.saveMenu(owningTruck);

        // when
        final String uri = BASE_URI + String.format("/%d/description", savedMenu.getId());
        final MenuDescriptionRequest request = new MenuDescriptionRequest("some description");
        final ValidatableResponse response = putWithToken(uri, token, request);

        // then
        response.statusCode(NO_CONTENT.value());
    }
}
