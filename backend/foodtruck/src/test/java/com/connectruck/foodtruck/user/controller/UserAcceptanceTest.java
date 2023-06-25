package com.connectruck.foodtruck.user.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.connectruck.foodtruck.common.testbase.AcceptanceTestBase;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.Role;
import com.connectruck.foodtruck.user.dto.UserRequest;
import com.connectruck.foodtruck.user.dto.UsernameRequest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class UserAcceptanceTest extends AcceptanceTestBase {

    @DisplayName("사장님 회원으로 가입한다.")
    @Test
    void create() {
        // given
        final UserRequest request = new UserRequest("test", "test1234!", "01012345678", Role.OWNER);

        // when
        final ValidatableResponse response = post("/api/users", request);

        // then
        response.statusCode(CREATED.value())
                .header("Location", "/api/users/me");
    }

    @DisplayName("사용 가능한 아이디인지 검사한다.")
    @Nested
    class checkUsername {

        @DisplayName("사용 가능하면 true를 반환한다.")
        @Test
        void returnTrue_whenUsernameAvailable() {
            // given
            final UsernameRequest request = new UsernameRequest("test");

            // when
            final ValidatableResponse response = post("/api/users/check-username", request);

            // then
            response.statusCode(OK.value())
                    .body("isAvailable", equalTo(true));
        }

        @DisplayName("사용 중인 아이디이면 false를 반환한다.")
        @Test
        void returnFalse_whenUsernameAlreadyExists() {
            // given
            final String existingUsername = "test";
            dataSetup.saveAccount(Account.ofNew(existingUsername, "test1234!", "01000000000", Role.OWNER));

            final UsernameRequest request = new UsernameRequest(existingUsername);

            // when
            final ValidatableResponse response = post("/api/users/check-username", request);

            // then
            response.statusCode(OK.value())
                    .body("isAvailable", equalTo(false));
        }
    }
}
