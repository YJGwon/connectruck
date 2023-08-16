package com.connectruck.foodtruck.notification.domain.push;

import org.springframework.data.repository.Repository;

public interface PushSubscriberRepository extends Repository<PushSubscriber, Long> {

    PushSubscriber save(PushSubscriber pushSubscriber);
}
