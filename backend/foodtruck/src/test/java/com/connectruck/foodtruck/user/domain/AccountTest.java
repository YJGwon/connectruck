package com.connectruck.foodtruck.user.domain;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.connectruck.foodtruck.common.exception.InvalidFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AccountTest {

    @DisplayName("휴대폰 번호 형식이 잘못되었을 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"11012341234", "01212341234", "010121234", "010a1231234", "010-1234-1234"})
    void ofNew_throwsException_whenPhoneInvalid(final String invalidPhone) {
        assertThatExceptionOfType(InvalidFormatException.class)
                .isThrownBy(() -> Account.ofNew("test", "test1234!", invalidPhone, Role.OWNER))
                .withMessageContaining("휴대폰 번호 형식");
    }
}
