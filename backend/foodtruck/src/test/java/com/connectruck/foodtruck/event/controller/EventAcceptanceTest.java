package com.connectruck.foodtruck.event.controller;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpStatus.OK;

import com.connectruck.foodtruck.common.testbase.AcceptanceTestBase;
import com.connectruck.foodtruck.truck.domain.Truck;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class EventAcceptanceTest extends AcceptanceTestBase {

    private static final String BASE_URI = "/api/events";

    @DisplayName("행사 참가 푸드트럭 목록 조회")
    @Nested
    class findAttendingTrucks {

        private static final String URI_FORMAT = BASE_URI + "/%d/trucks";

        @DisplayName("특정 페이지를 조회한다.")
        @Test
        void perPage() {
            // given
            final long eventId = 1L;
            final int page = 1;
            final int size = 2;

            dataSetup.saveTruck();
            dataSetup.saveTruck();
            final Truck expected = dataSetup.saveTruck();

            // when
            final String uri = String.format(URI_FORMAT, eventId);
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
            final ValidatableResponse response = get(String.format(URI_FORMAT, eventId));

            // then
            response.statusCode(OK.value())
                    .body("page.size", equalTo(20))
                    .body("page.currentPage", equalTo(0));
        }
    }
}
