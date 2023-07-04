package com.connectruck.foodtruck.menu.controller;

import static org.springframework.http.HttpStatus.OK;

import com.connectruck.foodtruck.common.testbase.AcceptanceTestBase;
import com.connectruck.foodtruck.truck.domain.Truck;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MenuAcceptanceTest extends AcceptanceTestBase {

    private static final String BASE_URI_FORMAT = "/api/trucks/%d/menus";

    @DisplayName("푸드트럭 메뉴 조회")
    @Nested
    class findMenuOfTruck {

        @Test
        void success() {
            // given
            final Truck savedTruck = dataSetup.saveTruck();

            // when
            final ValidatableResponse response = get(String.format(BASE_URI_FORMAT, savedTruck.getId()));

            // then
            response.statusCode(OK.value());
        }
    }
}
