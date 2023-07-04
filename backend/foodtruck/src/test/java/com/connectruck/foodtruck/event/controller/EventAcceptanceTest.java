package com.connectruck.foodtruck.event.controller;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.OK;

import com.connectruck.foodtruck.common.testbase.AcceptanceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class EventAcceptanceTest extends AcceptanceTestBase {

    private static final String BASE_URI = "/api/events";

    @DisplayName("행사 정보 조회")
    @Nested
    class findOneEvent {

        private static final String URI_FORMAT = BASE_URI + "/%d";

        @DisplayName("특정 행사의 정보를 id로 조회한다.")
        @Test
        void byId() {
            // given
            final Event expected = dataSetup.saveEvent(밤도깨비_야시장.create());

            // when
            final ValidatableResponse response = get(String.format(URI_FORMAT, expected.getId().intValue()));

            // then
            response.statusCode(OK.value())
                    .body("id", equalTo(expected.getId().intValue()))
                    .body("name", equalTo(expected.getName()));
        }
    }
}
