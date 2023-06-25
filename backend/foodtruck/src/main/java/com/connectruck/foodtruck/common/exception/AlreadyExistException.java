package com.connectruck.foodtruck.common.exception;

public class AlreadyExistException extends ClientException {

    private static final String TITLE = "이미 존재하는 값입니다.";
    private static final String DETAIL_FORMAT = "이미 존재하는 %s입니다. 입력값: %s";

    private AlreadyExistException(final String detail) {
        super(TITLE, detail);
    }

    public static AlreadyExistException withInputValue(final String parameterName, final String inputValue) {
        return new AlreadyExistException(String.format(DETAIL_FORMAT, parameterName, inputValue));
    }
}
