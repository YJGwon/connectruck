package com.connectruck.foodtruck.auth.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.connectruck.foodtruck.auth.controller.testcontroller.AuthTestController;
import com.connectruck.foodtruck.auth.controller.testcontroller.AuthorizationTestController;
import com.connectruck.foodtruck.auth.service.AuthService;
import com.connectruck.foodtruck.auth.support.JwtTokenProvider;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.AccountRepository;
import com.connectruck.foodtruck.user.domain.Role;
import java.util.Optional;
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

@WebMvcTest(controllers = {AuthTestController.class, AuthorizationTestController.class})
@Import({JwtTokenProvider.class, AuthService.class})
class AuthorizationInterceptorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private AccountRepository accountRepository;

    @DisplayName("인가가 필요하지 않은 요청에 대해서는 검증하지 않는다.")
    @Test
    void success_whenAuthorizationNotRequired() throws Exception {
        // given & when
        final ResultActions resultActions = mockMvc.perform(get("/api/no-auth"))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk());
    }

    @DisplayName("사장님 권한이 필요한 요청에 대해 권한 검증")
    @Nested
    class validateRole_whenAuthorizationRequired {

        private static final String URI_METHOD_ANNOTATED = "/api/authorization";
        private static final String URI_TYPE_ANNOTATED = "/api/type-authorization";
        private static final String PREFIX_BEARER = "Bearer ";

        private static final long ID = 1L;
        private static final Role REQUIRED_ROLE = Role.OWNER;

        private final String validToken = jwtTokenProvider.create(Long.toString(ID), REQUIRED_ROLE.name());

        @DisplayName("하여 권한이 있으면 요청을 처리한다.")
        @Test
        void success() throws Exception {
            // given
            setAccountRole(REQUIRED_ROLE);

            // when
            final ResultActions resultActions = mockMvc.perform(get(URI_METHOD_ANNOTATED)
                            .header(HttpHeaders.AUTHORIZATION, PREFIX_BEARER + validToken))
                    .andDo(print());

            // then
            resultActions.andExpect(status().isOk());
        }

        @DisplayName("할 때, 토큰이 유효하지 않으면 예외가 발생한다.")
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

        @DisplayName("할 때, 사장님 권한이 없으면 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {URI_METHOD_ANNOTATED, URI_TYPE_ANNOTATED})
        void returnForbidden_whenNotOwnerToken(final String uri) throws Exception {
            // given
            final String tokenWithoutRole = jwtTokenProvider.create(Long.toString(ID), "");

            // when
            final ResultActions resultActions = mockMvc.perform(get(uri)
                            .header(HttpHeaders.AUTHORIZATION, PREFIX_BEARER + tokenWithoutRole))
                    .andDo(print());

            // then
            resultActions.andExpect(status().isForbidden());
        }

        @DisplayName("할 때, 토큰에만 사장님 권한이 있으면 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {URI_METHOD_ANNOTATED, URI_TYPE_ANNOTATED})
        void returnForbidden_whenNotOwnerAccount(final String uri) throws Exception {
            // given
            setAccountRole(Role.NONE);

            // when
            final ResultActions resultActions = mockMvc.perform(get(uri)
                            .header(HttpHeaders.AUTHORIZATION, PREFIX_BEARER + validToken))
                    .andDo(print());

            // then
            resultActions.andExpect(status().isForbidden());
        }

        private void setAccountRole(final Role role) {
            final Account account = Account.ofNew("username", "test1234!", "01000000000", role);
            when(accountRepository.findById(1L))
                    .thenReturn(Optional.of(account));
        }
    }
}
