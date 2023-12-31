package com.connectruck.foodtruck.common.exception;

public class NotFoundException extends CustomException {

    private static final String TITLE_FORMAT = "존재하지 않는 %s입니다.";
    private static final String DETAIL_FORMAT = "%s이(가) 존재하지 않습니다. 입력값(%s): %s";

    private NotFoundException(final String title, final String detail) {
        super(title, detail);
    }

    public static NotFoundException of(final String targetName, final String paramName, final Object input) {
        final String title = String.format(TITLE_FORMAT, targetName);
        final String detail = String.format(DETAIL_FORMAT, targetName, paramName, input);
        return new NotFoundException(title, detail);
    }
}
