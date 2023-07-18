package com.connectruck.foodtruck.notification.exception;

public class NotificationException extends RuntimeException {

    public NotificationException(final Throwable cause) {
        super("알림 전송에 실패하였습니다.", cause);
    }
}
