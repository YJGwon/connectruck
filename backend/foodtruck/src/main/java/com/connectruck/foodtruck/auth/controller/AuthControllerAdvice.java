package com.connectruck.foodtruck.auth.controller;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.connectruck.foodtruck.auth.exception.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AuthControllerAdvice {

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse handleAuthenticationException(final AuthenticationException e) {
        log.error(e.getMessage());
        return ErrorResponse.builder(e, UNAUTHORIZED, e.getMessage())
                .title(e.getTitle())
                .build();
    }
}
