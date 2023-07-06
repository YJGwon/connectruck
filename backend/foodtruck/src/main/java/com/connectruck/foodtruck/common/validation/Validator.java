package com.connectruck.foodtruck.common.validation;

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
}
