package com.connectruck.foodtruck.menu.controller;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpStatus.OK;

import com.connectruck.foodtruck.common.testbase.AcceptanceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.truck.domain.Participation;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MenuAcceptanceTest extends AcceptanceTestBase {

    private static final String BASE_URI_FORMAT = "/api/trucks/%d/menus";

    @DisplayName("참가 푸드트럭 메뉴 목록 조회")
    @Nested
    class findMenusOfTruck {

        @DisplayName("참가 푸드트럭의 id로 메뉴 목록을 조회한다.")
        @Test
        void byParticipationId() {
            // given
            final Event event = 밤도깨비_야시장.create();
            dataSetup.saveEvent(event);

            final Participation savedParticipation = dataSetup.saveParticipation(event);
            final Menu expected = dataSetup.saveMenu(savedParticipation);

            // when
            final ValidatableResponse response = get(String.format(BASE_URI_FORMAT, savedParticipation.getId()));

            // then
            response.statusCode(OK.value())
                    .body("menus", hasSize(1))
                    .body("menus.id", contains(expected.getId().intValue()))
                    .body("menus.name", contains(expected.getName()))
                    .body("menus.price", contains(expected.getPrice().intValue()));
        }
    }
}
