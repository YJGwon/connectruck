package com.connectruck.foodtruck.auth.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.connectruck.foodtruck.auth.dto.SignInRequest;
import com.connectruck.foodtruck.auth.exception.SignInFailedException;
import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.user.domain.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthServiceTest extends ServiceTestBase {

    @Autowired
    private AuthService authService;

    @DisplayName("로그인")
    @Nested
    class signIn {

        private final Account existingAccount = dataSetup.saveAccount();

        @DisplayName("아이디가 잘못되었을 경우 예외가 발생한다.")
        @Test
        void throwsException_whenWrongUsername() {
            // given
            final String wrongUsername = existingAccount.getUsername() + "wrong";
            final SignInRequest request = new SignInRequest(wrongUsername, existingAccount.getPassword().getValue());

            // when & then
            assertThatExceptionOfType(SignInFailedException.class)
                    .isThrownBy(() -> authService.signIn(request));
        }

        @DisplayName("비밀번호가 일치하지 않을 경우 예외가 발생한다.")
        @Test
        void throwsException_whenWrongPassword() {
            // given
            final String wrongPassword = existingAccount.getPassword().getValue() + "wrong";
            final SignInRequest request = new SignInRequest(existingAccount.getUsername(), wrongPassword);

            // when & then
            assertThatExceptionOfType(SignInFailedException.class)
                    .isThrownBy(() -> authService.signIn(request));
        }
    }
}
