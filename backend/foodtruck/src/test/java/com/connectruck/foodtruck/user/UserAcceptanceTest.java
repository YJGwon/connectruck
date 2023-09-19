package com.connectruck.foodtruck.user;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.LOCATION;
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
        // given & when
        final UserRequest request = new UserRequest(username, password, phone, Role.OWNER);
        final ValidatableResponse response = post(BASE_URI, request);

        // then
        response.statusCode(CREATED.value())
                .header(LOCATION, BASE_URI + "/me");
    }

    @DisplayName("사용 가능한 아이디인지 검사")
    @Nested
    class checkUsername {

        private static final String URI = BASE_URI + "/check-username";

        @DisplayName("하여 사용 가능할 경우 true를 반환한다.")
        @Test
        void returnTrue_whenUsernameAvailable() {
            // given & when
            final UsernameRequest request = new UsernameRequest(username);
            final ValidatableResponse response = post(URI, request);

            // then
            response.statusCode(OK.value())
                    .body("isAvailable", equalTo(true));
        }

        @DisplayName("하여 사용 중인 아이디이면 false를 반환한다.")
        @Test
        void returnFalse_whenUsernameAlreadyExists() {
            // given
            final Account existingUser = dataSetup.saveOwnerAccount();

            // when
            final UsernameRequest request = new UsernameRequest(existingUser.getUsername());
            final ValidatableResponse response = post(URI, request);

            // then
            response.statusCode(OK.value())
                    .body("isAvailable", equalTo(false));
        }
    }

    @DisplayName("사용 가능한 휴대폰 번호인지 검사")
    @Nested
    class checkPhone {

        private static final String URI = BASE_URI + "/check-phone";

        @DisplayName("하여 사용 가능하면 true를 반환한다.")
        @Test
        void returnTrue_whenPhoneAvailable() {
            // given & when
            final PhoneRequest request = new PhoneRequest(phone);
            final ValidatableResponse response = post(URI, request);

            // then
            response.statusCode(OK.value())
                    .body("isAvailable", equalTo(true));
        }

        @DisplayName("하여 사용 중인 휴대폰 번호이면 false를 반환한다.")
        @Test
        void returnFalse_whenPhoneAlreadyExists() {
            // given
            final Account existingUser = dataSetup.saveOwnerAccount();

            // when
            final PhoneRequest request = new PhoneRequest(existingUser.getPhone());
            final ValidatableResponse response = post(URI, request);

            // then
            response.statusCode(OK.value())
                    .body("isAvailable", equalTo(false));
        }
    }
}
