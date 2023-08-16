package com.connectruck.foodtruck.notification.domain.push;

import org.springframework.data.repository.Repository;

public interface PushSubscriptionRepository extends Repository<PushSubscription, Long> {

    PushSubscription save(PushSubscription pushSubscription);
}
