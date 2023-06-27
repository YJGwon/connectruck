package com.connectruck.foodtruck.auth.config;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.connectruck.foodtruck.auth.service.AuthService;
import com.connectruck.foodtruck.auth.support.JwtTokenProvider;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.AccountRepository;
import com.connectruck.foodtruck.user.domain.Role;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(controllers = AuthTestController.class)
@Import({JwtTokenProvider.class, AuthService.class})
class AuthorizationConfigTest {

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

        private static final String URI = "/api/authorization";
        private static final String PREFIX_BEARER = "Bearer ";

        private static final long ID = 1L;
        private static final Role REQUIRED_ROLE = Role.OWNER;

        private final String validToken = jwtTokenProvider.create(Long.toString(ID), REQUIRED_ROLE.name());

        @DisplayName("사장님 권한이 있으면 요청에 성공한다.")
        @Test
        void success() throws Exception {
            // given
            setAccountRole(REQUIRED_ROLE);

            // when
            final ResultActions resultActions = mockMvc.perform(get(URI)
                            .header(HttpHeaders.AUTHORIZATION, PREFIX_BEARER + validToken))
                    .andDo(print());

            // then
            resultActions.andExpect(status().isOk());
        }

        @DisplayName("사장님 권한이 없으면 요청에 실패한다.")
        @Test
        void returnForbidden_whenNotOwnerToken() throws Exception {
            // given
            final String tokenWithoutRole = jwtTokenProvider.create(Long.toString(ID), "");

            // when
            final ResultActions resultActions = mockMvc.perform(get(URI)
                            .header(HttpHeaders.AUTHORIZATION, PREFIX_BEARER + tokenWithoutRole))
                    .andDo(print());

            // then
            resultActions.andExpect(status().isForbidden());
        }

        @DisplayName("토큰에만 사장님 권한이 있으면 요청에 실패한다.")
        @Test
        void returnForbidden_whenNotOwnerAccount() throws Exception {
            // given
            setAccountRole(Role.NONE);

            // when
            final ResultActions resultActions = mockMvc.perform(get(URI)
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
