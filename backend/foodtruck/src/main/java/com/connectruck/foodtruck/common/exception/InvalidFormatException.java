package com.connectruck.foodtruck.common.exception;

public class InvalidFormatException extends ClientException {

    private static final String TITLE = "입력값의 형식이 잘못되었습니다.";
    private static final String DETAIL_WITH_INPUT_VALUE_FORMAT = "%s 형식을 확인하세요. 입력값: %s";
    private static final String DETAIL_WITH_FORMAT_DESCRIPTION_FORMAT = "올바른 %s 형식: %s\n입력값: %s";

    private InvalidFormatException(final String detail) {
        super(TITLE, detail);
    }

    public static InvalidFormatException withInputValue(final String parameterName, final String input) {
        return new InvalidFormatException(String.format(DETAIL_WITH_INPUT_VALUE_FORMAT, parameterName, input));
    }

    public static InvalidFormatException withFormatDescription(final String parameterName,
                                                               final String formatDescription,
                                                               final String input) {
        return new InvalidFormatException(String.format(
                DETAIL_WITH_FORMAT_DESCRIPTION_FORMAT,
                parameterName, formatDescription, input));
    }
}
