package com.connectruck.foodtruck.auth.exception;

import com.connectruck.foodtruck.common.exception.CustomException;

public class AuthorizationException extends CustomException {

    private static final String TITLE = "권한이 없습니다.";
    private static final String DETAIL_FORMAT = "권한이 없습니다. (현재 사용자 권한 : %s)";

    public AuthorizationException(final String role) {
        super(TITLE, String.format(DETAIL_FORMAT, role));
    }
}
