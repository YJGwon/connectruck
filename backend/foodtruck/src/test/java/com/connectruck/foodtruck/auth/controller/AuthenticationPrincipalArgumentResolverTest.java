package com.connectruck.foodtruck.auth.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.connectruck.foodtruck.auth.controller.testcontroller.AuthTestController;
import com.connectruck.foodtruck.auth.service.AuthService;
import com.connectruck.foodtruck.auth.support.JwtTokenProvider;
import com.connectruck.foodtruck.user.domain.AccountRepository;
import com.connectruck.foodtruck.user.domain.Role;
import org.junit.jupiter.api.DisplayName;
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
public class AuthenticationPrincipalArgumentResolverTest {

    private static final String PREFIX_BEARER = "Bearer ";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private AccountRepository accountRepository;

    @DisplayName("사용자 식별 정보가 필요한 요청에 대해 token에서 사용자 id를 추출한다.")
    @Test
    void resolve_whenPrincipalRequired() throws Exception {
        // given
        final long id = 1L;
        final String validToken = jwtTokenProvider.create(Long.toString(id), Role.OWNER.name());

        // when
        final ResultActions resultActions = mockMvc.perform(get("/api/resolve-principal")
                        .header(HttpHeaders.AUTHORIZATION, PREFIX_BEARER + validToken))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId").value(1));
    }
}
