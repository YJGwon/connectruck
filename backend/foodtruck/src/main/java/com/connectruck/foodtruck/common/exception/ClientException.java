package com.connectruck.foodtruck.common.exception;

import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {

    private final String title;

    public ClientException(final String title, final String detail) {
        super(detail);
        this.title = title;
    }
}
