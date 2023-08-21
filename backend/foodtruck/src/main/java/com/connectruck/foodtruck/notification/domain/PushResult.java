package com.connectruck.foodtruck.notification.domain;

import java.util.List;

public record PushResult(
        int failureCount,
        List<String> failedTokens
) {

    public static final PushResult SUCCESS = new PushResult(0, List.of());
}
