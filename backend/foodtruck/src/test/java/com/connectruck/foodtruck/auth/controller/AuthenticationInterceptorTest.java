package com.connectruck.foodtruck.auth.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.connectruck.foodtruck.auth.controller.testcontroller.AuthTestController;
import com.connectruck.foodtruck.auth.controller.testcontroller.AuthenticationTestController;
import com.connectruck.foodtruck.auth.service.AuthService;
import com.connectruck.foodtruck.auth.support.JwtTokenProvider;
import com.connectruck.foodtruck.user.domain.AccountRepository;
import com.connectruck.foodtruck.user.domain.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(controllers = {AuthTestController.class, AuthenticationTestController.class})
@Import({JwtTokenProvider.class, AuthService.class})
class AuthenticationInterceptorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private AccountRepository accountRepository;

    @DisplayName("인증이 필요하지 않은 요청에 대해서는 검증하지 않는다.")
    @Test
    void success_whenAuthenticationNotRequired() throws Exception {
        // given & when
        final ResultActions resultActions = mockMvc.perform(get("/api/no-auth"))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk());
    }

    @DisplayName("인증이 필요한 요청에 대해 토큰 검증")
    @Nested
    class validateToken_whenAuthenticationRequired {

        private static final String URI_METHOD_ANNOTATED = "/api/authentication";
        private static final String URI_TYPE_ANNOTATED = "/api/type-authentication";
        private static final String PREFIX_BEARER = "Bearer ";

        private final String validToken = jwtTokenProvider.create("1", Role.OWNER.name());

        @DisplayName("토큰이 유효하면 요청에 성공한다.")
        @ParameterizedTest
        @ValueSource(strings = {URI_METHOD_ANNOTATED, URI_TYPE_ANNOTATED})
        void success(final String uri) throws Exception {
            // given & when
            final ResultActions resultActions = mockMvc.perform(get(uri)
                            .header(HttpHeaders.AUTHORIZATION, PREFIX_BEARER + validToken))
                    .andDo(print());

            // then
            resultActions.andExpect(status().isOk());
        }

        @DisplayName("토큰이 없으면 요청에 실패한다.")
        @ParameterizedTest
        @ValueSource(strings = {URI_METHOD_ANNOTATED, URI_TYPE_ANNOTATED})
        void returnUnauthorized_withoutToken(final String uri) throws Exception {
            // given & when
            final ResultActions resultActions = mockMvc.perform(get(uri))
                    .andDo(print());

            // then
            resultActions
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("title").value("사용자 인증에 실패하였습니다."))
                    .andExpect(jsonPath("detail").value("토큰이 존재하지 않습니다."));
        }

        @DisplayName("토큰 형식이 잘못되었으면 요청에 실패한다.")
        @ParameterizedTest
        @ValueSource(strings = {URI_METHOD_ANNOTATED, URI_TYPE_ANNOTATED})
        void returnUnauthorized_whenTokenIsNotBearer(final String uri) throws Exception {
            // given & when
            final ResultActions resultActions = mockMvc.perform(get(uri)
                            .header(HttpHeaders.AUTHORIZATION, validToken))
                    .andDo(print());

            // then
            resultActions
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("title").value("사용자 인증에 실패하였습니다."))
                    .andExpect(jsonPath("detail").value("식별할 수 없는 형식의 토큰입니다."));
        }

        @DisplayName("토큰이 유효하지 않으면 요청에 실패한다.")
        @ParameterizedTest
        @ValueSource(strings = {URI_METHOD_ANNOTATED, URI_TYPE_ANNOTATED})
        void returnUnauthorized_whenTokenInvalid(final String uri) throws Exception {
            // given & when
            final ResultActions resultActions = mockMvc.perform(get(uri)
                            .header(HttpHeaders.AUTHORIZATION, PREFIX_BEARER + "invalidToken"))
                    .andDo(print());

            // then
            resultActions
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("title").value("사용자 인증에 실패하였습니다."))
                    .andExpect(jsonPath("detail").value("유효하지 않은 토큰입니다."));
        }
    }
}
