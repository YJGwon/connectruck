package com.connectruck.foodtruck.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EncodedPasswordTest {

    @DisplayName("평문 비밀번호를 암호화한다.")
    @Test
    void fromPlainText() {
        // given
        final String plainText = "test1234!";

        // when
        final EncodedPassword encodedPassword = EncodedPassword.fromPlainText(plainText);

        // then
        assertThat(encodedPassword.isSamePassword(plainText)).isTrue();
    }
}
