package com.connectruck.foodtruck.notification.domain.push;

import java.util.List;

public interface PushSender {

    PushResult send(PushNotification pushNotification, List<PushSubscription> pushSubscriptions);
}
