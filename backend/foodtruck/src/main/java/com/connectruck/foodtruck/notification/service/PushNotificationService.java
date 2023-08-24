package com.connectruck.foodtruck.notification.service;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.notification.domain.PushNotification;
import com.connectruck.foodtruck.notification.domain.PushResult;
import com.connectruck.foodtruck.notification.domain.PushSender;
import com.connectruck.foodtruck.notification.domain.PushSubscription;
import com.connectruck.foodtruck.notification.domain.PushSubscriptionRepository;
import com.connectruck.foodtruck.notification.dto.PushSubscribeRequest;
import com.connectruck.foodtruck.notification.dto.PushUnsubscribeRequest;
import com.connectruck.foodtruck.order.message.OrderMessage;
import com.connectruck.foodtruck.truck.domain.TruckRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PushNotificationService {

    private final PushSender pushSender;

    private final PushSubscriptionRepository pushSubscriptionRepository;
    private final TruckRepository truckRepository;

    @Transactional
    public void subscribeOrders(final PushSubscribeRequest request, final Long ownerId) {
        final String token = request.token();
        final Long truckId = truckRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> NotFoundException.of("소유한 푸드트럭", "ownerId", ownerId))
                .getId();

        if (pushSubscriptionRepository.existsByTokenAndTruckId(token, truckId)) {
            return;
        }
        final PushSubscription pushSubscription = PushSubscription.ofNew(token, truckId);
        pushSubscriptionRepository.save(pushSubscription);
    }

    @Transactional
    public void unsubscribeOrders(final PushUnsubscribeRequest request, final Long ownerId) {
        final String token = request.token();
        final Long truckId = truckRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> NotFoundException.of("소유한 푸드트럭", "ownerId", ownerId))
                .getId();

        pushSubscriptionRepository.deleteByTokenAndTruckId(token, truckId);
    }

    @Async
    @Transactional
    public void notifyOrderToOwner(final OrderMessage orderMessage) {
        final List<PushSubscription> subscriptions = pushSubscriptionRepository.findByTruckId(orderMessage.truckId());
        if (subscriptions.isEmpty()) {
            return;
        }

        final PushNotification pushNotification = PushNotification.ofOrder(
                orderMessage.orderId(), orderMessage.status()
        );
        final PushResult result = pushSender.send(pushNotification, subscriptions);
        for (String failedToken : result.failedTokens()) {
            pushSubscriptionRepository.deleteByTokenAndTruckId(failedToken, orderMessage.truckId());
        }
    }
}
