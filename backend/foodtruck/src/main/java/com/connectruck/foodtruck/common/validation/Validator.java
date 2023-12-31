package com.connectruck.foodtruck.common.validation;

import static com.connectruck.foodtruck.common.validation.ValidationMessage.LONGER_THAN_MAX_LENGTH;

import com.connectruck.foodtruck.common.exception.ClientException;
import com.connectruck.foodtruck.common.exception.InvalidFormatException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static final Pattern PATTERN_PHONE = Pattern.compile(FormatText.PHONE);

    public static void validatePhone(final String phone) {
        final Matcher matcher = PATTERN_PHONE.matcher(phone);
        if (!matcher.matches()) {
            throw InvalidFormatException.withInputValue("휴대폰 번호", phone);
        }
    }

    public static void validateMaxLength(final String input, final int maxLength) {
        if (input.length() > maxLength) {
            throw new ClientException(LONGER_THAN_MAX_LENGTH, String.format("최대 글자 수는 %d자 입니다.", maxLength));
        }
    }
}
