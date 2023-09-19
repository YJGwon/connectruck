package com.connectruck.foodtruck.auth.controller;

import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.connectruck.foodtruck.auth.dto.SignInRequest;
import com.connectruck.foodtruck.common.testbase.ControllerTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.test.web.servlet.ResultActions;

public class AuthControllerTest extends ControllerTestBase {

    private static final String BASE_URI = "/api/auth";

    @DisplayName("로그인")
    @Nested
    class signIn {

        private static final String URI = BASE_URI + "/signin";

        @DisplayName("할 때, 아이디가 비어있으면 Bad Request를 응답한다.")
        @ParameterizedTest
        @NullAndEmptySource
        void returnBadRequest_whenUsernameIsBlank(final String blank) throws Exception {
            // given & when
            final SignInRequest request = new SignInRequest(blank, "test1234!");
            final ResultActions resultActions = performPost(URI, request);

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("title").value("요청 본문이 올바르지 않습니다."))
                    .andExpect(jsonPath("detail", stringContainsInOrder("필수", "아이디")));
        }

        @DisplayName("할 때, 비밀번호가 비어있으면 Bad Request를 응답한다.")
        @ParameterizedTest
        @NullAndEmptySource
        void returnBadRequest_whenPasswordIsBlank(final String blank) throws Exception {
            // given & when
            final SignInRequest request = new SignInRequest("test", blank);
            final ResultActions resultActions = performPost(URI, request);

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("title").value("요청 본문이 올바르지 않습니다."))
                    .andExpect(jsonPath("detail", stringContainsInOrder("필수", "비밀번호")));
        }
    }
}
