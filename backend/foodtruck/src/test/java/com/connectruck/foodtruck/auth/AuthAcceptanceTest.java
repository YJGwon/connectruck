package com.connectruck.foodtruck.auth;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.OK;

import com.connectruck.foodtruck.auth.dto.SignInRequest;
import com.connectruck.foodtruck.common.testbase.AcceptanceTestBase;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.Role;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthAcceptanceTest extends AcceptanceTestBase {

    @DisplayName("로그인에 성공하고 토큰을 발급받는다.")
    @Test
    void signIn() {
        // given
        final String username = "test";
        final String password = "test1234!";
        dataSetup.saveAccount(Account.ofNew(username, password, "01000000000", Role.OWNER));

        final SignInRequest request = new SignInRequest(username, password);

        // when
        final ValidatableResponse response = post("/api/auth/signin", request);

        // then
        response.statusCode(OK.value())
                .body("accessToken", notNullValue());
    }
}
