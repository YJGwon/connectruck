package com.connectruck.foodtruck.user.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.connectruck.foodtruck.common.testbase.AcceptanceTestBase;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.Role;
import com.connectruck.foodtruck.user.dto.PhoneRequest;
import com.connectruck.foodtruck.user.dto.UserRequest;
import com.connectruck.foodtruck.user.dto.UsernameRequest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class UserAcceptanceTest extends AcceptanceTestBase {

    private static final String BASE_URI = "/api/users";
    private final String username = "test";
    private final String password = "test1234!";
    private final String phone = "01000000000";

    @DisplayName("사장님 회원으로 가입한다.")
    @Test
    void create() {
        // given
        final UserRequest request = new UserRequest(username, password, phone, Role.OWNER);

        // when
        final ValidatableResponse response = post(BASE_URI, request);

        // then
        response.statusCode(CREATED.value())
                .header("Location", BASE_URI + "/me");
    }

    @DisplayName("사용 가능한 아이디인지 검사한다.")
    @Nested
    class checkUsername {

        private static final String URI = BASE_URI + "/check-username";

        @DisplayName("사용 가능하면 true를 반환한다.")
        @Test
        void returnTrue_whenUsernameAvailable() {
            // given
            final UsernameRequest request = new UsernameRequest(username);

            // when
            final ValidatableResponse response = post(URI, request);

            // then
            response.statusCode(OK.value())
                    .body("isAvailable", equalTo(true));
        }

        @DisplayName("사용 중인 아이디이면 false를 반환한다.")
        @Test
        void returnFalse_whenUsernameAlreadyExists() {
            // given
            final String existingUsername = "test1";
            dataSetup.saveAccount(Account.ofNew(existingUsername, password, phone, Role.OWNER));

            final UsernameRequest request = new UsernameRequest(existingUsername);

            // when
            final ValidatableResponse response = post(URI, request);

            // then
            response.statusCode(OK.value())
                    .body("isAvailable", equalTo(false));
        }
    }

    @DisplayName("사용 가능한 휴대폰 번호인지 검사한다.")
    @Nested
    class checkPhone {

        private static final String URI = BASE_URI + "/check-phone";

        @DisplayName("사용 가능하면 true를 반환한다.")
        @Test
        void returnTrue_whenPhoneAvailable() {
            // given
            final PhoneRequest request = new PhoneRequest(phone);

            // when
            final ValidatableResponse response = post(URI, request);

            // then
            response.statusCode(OK.value())
                    .body("isAvailable", equalTo(true));
        }

        @DisplayName("사용 중인 휴대폰 번호이면 false를 반환한다.")
        @Test
        void returnFalse_whenPhoneAlreadyExists() {
            // given
            final String existingPhone = "01000000001";
            dataSetup.saveAccount(Account.ofNew(username, password, existingPhone, Role.OWNER));

            final PhoneRequest request = new PhoneRequest(existingPhone);

            // when
            final ValidatableResponse response = post(URI, request);

            // then
            response.statusCode(OK.value())
                    .body("isAvailable", equalTo(false));
        }
    }
}
