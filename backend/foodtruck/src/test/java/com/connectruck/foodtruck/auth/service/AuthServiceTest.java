package com.connectruck.foodtruck.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.connectruck.foodtruck.auth.dto.SignInRequest;
import com.connectruck.foodtruck.auth.dto.TokenResponse;
import com.connectruck.foodtruck.auth.exception.SignInFailedException;
import com.connectruck.foodtruck.auth.support.JwtTokenProvider;
import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthServiceTest extends ServiceTestBase {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("로그인")
    @Nested
    class signIn {

        final String username = "test";
        final String password = "test1234!";

        @DisplayName("한 뒤 access token을 발급받는다.")
        @Test
        void returnAccessToken() {
            // given
            final Account account = Account.ofNew(username, password, "01000000000", Role.OWNER);
            dataSetup.saveAccount(account);

            // when
            final SignInRequest request = new SignInRequest(username, password);
            final TokenResponse tokenResponse = authService.signIn(request);

            // then
            final String accessToken = tokenResponse.accessToken();
            final long extractedId = Long.parseLong(jwtTokenProvider.getSubject(accessToken));
            final Role extractedRole = Role.valueOf(jwtTokenProvider.getRole(accessToken));

            assertAll(
                    () -> assertThat(extractedId).isEqualTo(account.getId()),
                    () -> assertThat(extractedRole).isEqualTo(account.getRole())
            );
        }

        @DisplayName("할 때, 잘못된 아이디면 예외가 발생한다.")
        @Test
        void throwsException_whenWrongUsername() {
            final SignInRequest request = new SignInRequest("test", "test1234!");
            assertThatExceptionOfType(SignInFailedException.class)
                    .isThrownBy(() -> authService.signIn(request));
        }

        @DisplayName("할 때, 비밀번호가 일치하지 않으면 예외가 발생한다.")
        @Test
        void throwsException_whenWrongPassword() {
            // given
            final Account account = Account.ofNew(username, password, "01000000000", Role.OWNER);
            dataSetup.saveAccount(account);

            // when & then
            final String wrongPassword = password + "!";
            final SignInRequest request = new SignInRequest(username, wrongPassword);

            assertThatExceptionOfType(SignInFailedException.class)
                    .isThrownBy(() -> authService.signIn(request));
        }
    }
}
