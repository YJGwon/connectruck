package com.connectruck.foodtruck.notification.domain.push;

import java.util.Map;

public record PushNotification(
        String title,
        String body,
        Map<String, String> data
) {
}
