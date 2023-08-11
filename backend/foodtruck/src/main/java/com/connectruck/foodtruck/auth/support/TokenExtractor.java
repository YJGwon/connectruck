package com.connectruck.foodtruck.auth.support;

import com.connectruck.foodtruck.auth.exception.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

public class TokenExtractor {

    private static final String BEARER_TYPE = "Bearer";

    public static String extract(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        checkNotNull(token);
        checkType(token);
        return token.substring(BEARER_TYPE.length() + 1);
    }

    private static void checkNotNull(String token) {
        if (token == null) {
            throw new AuthenticationException("토큰이 존재하지 않습니다.");
        }
    }

    private static void checkType(String token) {
        if (!token.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
            throw new AuthenticationException("식별할 수 없는 형식의 토큰입니다.");
        }
    }
}
