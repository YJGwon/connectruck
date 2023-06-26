package com.connectruck.foodtruck.auth.exception;

import com.connectruck.foodtruck.common.exception.ClientException;

public class SignInFailedException extends ClientException {

    private static final String TITLE = "로그인에 실패했습니다.";
    private static final String DETAIL = "잘못된 아이디 또는 비밀번호입니다.";

    public SignInFailedException() {
        super(TITLE, DETAIL);
    }
}
