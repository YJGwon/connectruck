package com.connectruck.foodtruck.notification.domain.push;

import java.util.List;
import org.springframework.data.repository.Repository;

public interface PushSubscriptionRepository extends Repository<PushSubscription, Long> {

    PushSubscription save(PushSubscription pushSubscription);

    boolean existsByTokenAndTruckId(String token, Long truckId);

    List<PushSubscription> findByTruckId(Long truckId);

    void deleteByTokenAndTruckId(String token, Long truckId);
}
