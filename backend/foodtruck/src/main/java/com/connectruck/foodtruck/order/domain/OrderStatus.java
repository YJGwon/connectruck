package com.connectruck.foodtruck.order.domain;

public enum OrderStatus {

    CREATED("접수 대기"),
    COOKING("조리중"),
    COOKED("조리 완료"),
    COMPLETE("픽업 완료"),
    CANCELED("취소"),
    ALL("모두");

    private final String korean;

    OrderStatus(final String korean) {
        this.korean = korean;
    }

    public String toKorean() {
        return korean;
    }

    public boolean isInProgress() {
        return this != COMPLETE && this != CANCELED;
    }

    public boolean canBe(final OrderStatus status) {
        return switch (status) {
            case COOKING -> this == CREATED;
            case COOKED -> this == COOKING;
            case COMPLETE -> this == COOKED;
            case CANCELED -> this.isInProgress();
            default -> false;
        };
    }
}
