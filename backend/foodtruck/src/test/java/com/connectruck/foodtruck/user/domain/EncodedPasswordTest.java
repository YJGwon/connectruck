package com.connectruck.foodtruck.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.connectruck.foodtruck.common.exception.InvalidFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EncodedPasswordTest {

    @DisplayName("평문 비밀번호를 암호화한다.")
    @Nested
    class fromPlainText {

        @DisplayName("평문 비밀번호를 암호화한다.")
        @Test
        void success() {
            // given
            final String plainText = "test1234!";

            // when
            final EncodedPassword encodedPassword = EncodedPassword.fromPlainText(plainText);

            // then
            assertThat(encodedPassword.isSamePassword(plainText)).isTrue();
        }

        @DisplayName("비밀번호 형식이 잘못되었을 경우 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"new1234", "12345678!", "newpass!", "newpw1!", "123456789a123456789a123456789a!"})
        void throwsException_whenFormatInvalid(final String invalidText) {
            assertThatExceptionOfType(InvalidFormatException.class)
                    .isThrownBy(() -> EncodedPassword.fromPlainText(invalidText))
                    .withMessageContaining("비밀번호 형식");
        }
    }
}
