package com.connectruck.foodtruck.notification.domain;

import java.util.List;

public interface PushSender {

    PushResult send(PushNotification pushNotification, List<PushSubscription> pushSubscriptions);
}
