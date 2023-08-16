package com.connectruck.foodtruck.notification.service;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.notification.domain.push.PushSubscriber;
import com.connectruck.foodtruck.notification.domain.push.PushSubscriberRepository;
import com.connectruck.foodtruck.notification.dto.PushSubscribeRequest;
import com.connectruck.foodtruck.truck.domain.TruckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PushNotificationService {

    private final PushSubscriberRepository pushSubscriberRepository;
    private final TruckRepository truckRepository;

    @Transactional
    public void subscribeOrders(final PushSubscribeRequest request, final Long ownerId) {
        final Long truckId = truckRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> NotFoundException.of("소유한 푸드트럭", "ownerId", ownerId))
                .getId();

        final PushSubscriber pushSubscriber = PushSubscriber.ofNew(request.token(), truckId);
        pushSubscriberRepository.save(pushSubscriber);
    }
}
