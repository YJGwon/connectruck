package com.connectruck.foodtruck.common.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.connectruck.foodtruck.common.exception.ClientException;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler(ClientException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleClientException(final ClientException e) {
        log.error(e.getMessage());
        return ErrorResponse.builder(e, BAD_REQUEST, e.getMessage())
                .title(e.getTitle())
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleParameterException(final ConstraintViolationException e) {
        log.error(e.getMessage());
        return ErrorResponse.builder(e, BAD_REQUEST, e.getMessage())
                .title("요청 파라미터값이 올바르지 않습니다.")
                .build();
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class
    })
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleRequestException(final Exception e) {
        log.error(e.getMessage());
        final String message = extractMessage(e);
        return ErrorResponse.builder(e, BAD_REQUEST, message)
                .title("요청 본문이 올바르지 않습니다.")
                .build();
    }

    private String extractMessage(final Exception e) {
        if (Objects.requireNonNull(e) instanceof MethodArgumentNotValidException m) {
            return extractMessage(m.getFieldErrors());
        }
        return e.getMessage();
    }

    private String extractMessage(final List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));
    }
}
