package com.connectruck.foodtruck.user.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.connectruck.foodtruck.common.testbase.AcceptanceTestBase;
import com.connectruck.foodtruck.user.domain.Role;
import com.connectruck.foodtruck.user.dto.UserRequest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class UserAcceptanceTest extends AcceptanceTestBase {

    @DisplayName("회원 가입")
    @Nested
    class createUser {

        @DisplayName("사장님 회원으로 가입한다.")
        @Test
        void asOwner() {
            // given
            final UserRequest request = new UserRequest("test", "test1234!", "01012345678", Role.OWNER);

            // when
            final ValidatableResponse response = post("/api/users", request);

            // then
            response.statusCode(CREATED.value())
                    .header("Location", "/api/users/me");
        }
    }
}
