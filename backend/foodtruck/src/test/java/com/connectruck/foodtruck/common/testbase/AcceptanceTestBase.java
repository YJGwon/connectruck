package com.connectruck.foodtruck.common.testbase;

import com.connectruck.foodtruck.auth.dto.SignInRequest;
import com.connectruck.foodtruck.auth.dto.TokenResponse;
import com.connectruck.foodtruck.common.fixture.DataSetup;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql("classpath:truncate.sql")
public abstract class AcceptanceTestBase {

    @Autowired
    protected DataSetup dataSetup;
    @LocalServerPort
    int port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    protected ValidatableResponse get(final String uri) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all();
    }

    protected ValidatableResponse getWithToken(final String uri, final String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all();
    }

    protected ValidatableResponse post(final String uri, final Record body) {
        return RestAssured
                .given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post(uri)
                .then().log().all();
    }

    protected ValidatableResponse postWithToken(final String uri, final String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post(uri)
                .then().log().all();
    }

    protected String loginAndGetToken(final String username, final String password) {
        final SignInRequest request = new SignInRequest(username, password);
        return post("/api/auth/signin", request)
                .extract()
                .body()
                .as(TokenResponse.class)
                .accessToken();
    }
}
