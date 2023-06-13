package com.connectruck.foodtruck.common.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import jakarta.validation.ConstraintViolationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleParameterException(final Exception e) {
        return ErrorResponse.builder(e, BAD_REQUEST, e.getMessage())
                .title("요청 파라미터값이 올바르지 않습니다.")
                .build();
    }
}
