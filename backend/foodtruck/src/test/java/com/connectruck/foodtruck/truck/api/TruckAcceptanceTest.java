package com.connectruck.foodtruck.truck.api;

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

public class TruckAcceptanceTest extends AcceptanceTestBase {

    @DisplayName("푸드트럭 전체 목록 조회")
    @Nested
    class findAllTrucks {

        @DisplayName("특정 페이지를 조회한다.")
        @Test
        void perPage() {
            // given
            dataSetup.saveTruck();
            dataSetup.saveTruck();
            final Truck expected = dataSetup.saveTruck();

            // when
            final ValidatableResponse response = get("/api/trucks?page=1&size=2");

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
            // given & when
            final ValidatableResponse response = get("/api/trucks");

            // then
            response.statusCode(OK.value())
                    .body("page.size", equalTo(20))
                    .body("page.currentPage", equalTo(0));
        }
    }
}
