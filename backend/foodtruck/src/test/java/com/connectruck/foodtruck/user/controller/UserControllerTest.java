package com.connectruck.foodtruck.user.controller;

import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.connectruck.foodtruck.common.testbase.ControllerTestBase;
import com.connectruck.foodtruck.user.domain.Role;
import com.connectruck.foodtruck.user.dto.PhoneRequest;
import com.connectruck.foodtruck.user.dto.UserRequest;
import com.connectruck.foodtruck.user.dto.UsernameRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.web.servlet.ResultActions;

public class UserControllerTest extends ControllerTestBase {

    private static final String BASE_URI = "/api/users";
    private final String username = "test";
    private final String password = "test1234!";
    private final String phone = "01000000000";

    @DisplayName("아이디 검사 요청 시, 아이디가 비어있을 경우 Bad Request를 응답한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void checkUsername_returnBadRequest_whenUsernameIsBlank(final String blank) throws Exception {
        // given
        final UsernameRequest request = new UsernameRequest(blank);

        // when
        final ResultActions resultActions = performPost(BASE_URI + "/check-username", request);

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("title").value("요청 본문이 올바르지 않습니다."))
                .andExpect(jsonPath("detail", stringContainsInOrder("필수", "아이디")));
    }

    @DisplayName("휴대폰 번호 검사 요청 시, 휴대폰 번호 형식이 잘못되었을 경우 Bad Request를 응답한다.")
    @ParameterizedTest
    @ValueSource(strings = {"11012341234", "01212341234", "010121234", "010a1231234", "010-1234-1234"})
    void checkPhone_returnBadRequest_whenPhoneInvalid(final String invalidPhone) throws Exception {
        // given
        final PhoneRequest request = new PhoneRequest(invalidPhone);

        // when
        final ResultActions resultActions = performPost(BASE_URI + "/check-phone", request);

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("title").value("요청 본문이 올바르지 않습니다."))
                .andExpect(jsonPath("detail", stringContainsInOrder("형식", "휴대폰 번호")));
    }

    @DisplayName("회원 정보 생성")
    @Nested
    class create {

        @DisplayName("아이디가 비어있을 경우 Bad Request를 응답한다.")
        @ParameterizedTest
        @NullAndEmptySource
        void returnBadRequest_whenUsernameIsBlank(final String blank) throws Exception {
            // given
            final UserRequest request = new UserRequest(blank, password, phone, Role.OWNER);

            // when
            final ResultActions resultActions = performPost(BASE_URI, request);

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("title").value("요청 본문이 올바르지 않습니다."))
                    .andExpect(jsonPath("detail", stringContainsInOrder("필수", "아이디")));
        }

        @DisplayName("비밀번호 형식이 잘못되었을 경우 Bad Request를 응답한다.")
        @ParameterizedTest
        @ValueSource(strings = {"new1234", "12345678!", "newpass!", "newpw1!", "123456789a123456789a123456789a!"})
        void returnBadRequest_whenPasswordInvalid(final String invalidPassword) throws Exception {
            // given
            final UserRequest request = new UserRequest(username, invalidPassword, phone, Role.OWNER);

            // when
            final ResultActions resultActions = performPost(BASE_URI, request);

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("title").value("요청 본문이 올바르지 않습니다."))
                    .andExpect(jsonPath("detail", stringContainsInOrder("형식", "비밀번호")));
        }

        @DisplayName("휴대폰 번호 형식이 잘못되었을 경우 Bad Request를 응답한다.")
        @ParameterizedTest
        @ValueSource(strings = {"11012341234", "01212341234", "010121234", "010a1231234", "010-1234-1234"})
        void returnBadRequest_whenPhoneInvalid(final String invalidPhone) throws Exception {
            // given
            final UserRequest request = new UserRequest(username, password, invalidPhone, Role.OWNER);

            // when
            final ResultActions resultActions = performPost(BASE_URI, request);

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("title").value("요청 본문이 올바르지 않습니다."))
                    .andExpect(jsonPath("detail", stringContainsInOrder("형식", "휴대폰 번호")));
        }

        @DisplayName("계정 권한이 비어있는 경우 Bad Request를 응답한다.")
        @Test
        void returnBadRequest_whenRoleIsNull() throws Exception {
            // given
            final UserRequest request = new UserRequest(username, password, phone, null);

            // when
            final ResultActions resultActions = performPost(BASE_URI, request);

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("title").value("요청 본문이 올바르지 않습니다."))
                    .andExpect(jsonPath("detail", stringContainsInOrder("필수", "계정 권한")));
        }
    }
}
