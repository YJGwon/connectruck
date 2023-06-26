package com.connectruck.foodtruck.auth.exception;

import com.connectruck.foodtruck.common.exception.CustomException;

public class AuthenticationException extends CustomException {

    private static final String TITLE = "사용자 인증에 실패하였습니다.";

    public AuthenticationException(final String detail) {
        super(TITLE, detail);
    }
}
