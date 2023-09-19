package com.connectruck.foodtruck.common.validation;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.connectruck.foodtruck.common.exception.InvalidFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ValidatorTest {


    @DisplayName("휴대폰 번호 형식 검증")
    @Nested
    class validatePhone {

        @DisplayName("하여 형식에 맞으면 성공한다.")
        @ParameterizedTest
        @ValueSource(strings = {"01000000000", "01112341234", "0161231234", "01912341234"})
        void success(final String validPhone) {
            assertThatNoException()
                    .isThrownBy(() -> Validator.validatePhone(validPhone));
        }

        @DisplayName("할 때, 형식이 잘못되었으면 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"11012341234", "01212341234", "010121234", "010a1231234", "010-1234-1234"})
        void throwsException_whenPhoneInvalid(final String invalidPhone) {
            assertThatExceptionOfType(InvalidFormatException.class)
                    .isThrownBy(() -> Validator.validatePhone(invalidPhone))
                    .withMessageContaining("휴대폰 번호 형식");
        }
    }
}
