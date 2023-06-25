package com.connectruck.foodtruck.user.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.connectruck.foodtruck.common.testbase.ControllerTestBase;
import com.connectruck.foodtruck.user.domain.Role;
import com.connectruck.foodtruck.user.dto.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.web.servlet.ResultActions;

public class UserControllerTest extends ControllerTestBase {

    @DisplayName("회원 정보 생성")
    @Nested
    class create {

        private final String uri = "/api/users";

        private final String username = "test";
        private final String password = "test1234!";
        private final String phone = "01012341234";

        @DisplayName("아이디가 비어있을 경우 Bad Request를 응답한다.")
        @ParameterizedTest
        @NullAndEmptySource
        void returnBadRequest_whenUsernameIsBlank(final String blank) throws Exception {
            // given
            final UserRequest request = new UserRequest(blank, password, phone, Role.OWNER);

            // when
            final ResultActions resultActions = performPost(uri, request);

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("title").value("요청 본문이 올바르지 않습니다."));
        }

        @DisplayName("비밀번호 형식이 잘못되었을 경우 Bad Request를 응답한다.")
        @ParameterizedTest
        @ValueSource(strings = {"new1234", "12345678!", "newpass!", "newpw1!", "123456789a123456789a123456789a!"})
        void returnBadRequest_whenPasswordInvalid(final String invalidPassword) throws Exception {
            // given
            final UserRequest request = new UserRequest(username, invalidPassword, phone, Role.OWNER);

            // when
            final ResultActions resultActions = performPost(uri, request);

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("title").value("요청 본문이 올바르지 않습니다."));
        }

        @DisplayName("휴대폰 번호 형식이 잘못되었을 경우 Bad Request를 응답한다.")
        @ParameterizedTest
        @ValueSource(strings = {"11012341234", "01212341234", "010121234", "010a1231234", "010-1234-1234"})
        void returnBadRequest_whenPhoneInvalid(final String invalidPhone) throws Exception {
            // given
            final UserRequest request = new UserRequest(username, password, invalidPhone, Role.OWNER);

            // when
            final ResultActions resultActions = performPost(uri, request);

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("title").value("요청 본문이 올바르지 않습니다."));
        }

        @DisplayName("계정 권한이 비어있는 경우 Bad Request를 응답한다.")
        @Test
        void returnBadRequest_whenRoleIsNull() throws Exception {
            // given
            final UserRequest request = new UserRequest(username, password, phone, null);

            // when
            final ResultActions resultActions = performPost(uri, request);

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("title").value("요청 본문이 올바르지 않습니다."));
        }
    }
}
