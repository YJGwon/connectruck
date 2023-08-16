package com.connectruck.foodtruck.notification.service;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.notification.domain.push.PushSubscription;
import com.connectruck.foodtruck.notification.domain.push.PushSubscriptionRepository;
import com.connectruck.foodtruck.notification.dto.PushSubscribeRequest;
import com.connectruck.foodtruck.truck.domain.TruckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PushNotificationService {

    private final PushSubscriptionRepository pushSubscriptionRepository;
    private final TruckRepository truckRepository;

    @Transactional
    public void subscribeOrders(final PushSubscribeRequest request, final Long ownerId) {
        final Long truckId = truckRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> NotFoundException.of("소유한 푸드트럭", "ownerId", ownerId))
                .getId();

        final PushSubscription pushSubscription = PushSubscription.ofNew(request.token(), truckId);
        pushSubscriptionRepository.save(pushSubscription);
    }
}
