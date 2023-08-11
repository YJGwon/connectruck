package com.connectruck.foodtruck.common.exception;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {

    private final String title;

    public CustomException(final String title, final String detail) {
        super(detail);
        this.title = title;
    }
}
