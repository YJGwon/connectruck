package com.connectruck.foodtruck.notification.infra;

import com.connectruck.foodtruck.notification.domain.push.PushNotification;
import com.connectruck.foodtruck.notification.domain.push.PushResult;
import com.connectruck.foodtruck.notification.domain.push.PushSender;
import com.connectruck.foodtruck.notification.domain.push.PushSubscription;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FcmPushSender implements PushSender {

    private final FirebaseMessaging firebaseMessaging;

    @Override
    public PushResult send(final PushNotification pushNotification, final List<PushSubscription> pushSubscriptions) {
        final List<String> tokens = pushSubscriptions.stream()
                .map(PushSubscription::getToken)
                .toList();
        final MulticastMessage message = buildMessage(pushNotification, tokens);

        try {
            final BatchResponse batchResponse = firebaseMessaging.sendEachForMulticast(message);
            return getPushResult(batchResponse, tokens);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private MulticastMessage buildMessage(final PushNotification pushNotification,
                                          final List<String> tokens) {
        final Notification notification = Notification.builder()
                .setTitle(pushNotification.title())
                .setBody(pushNotification.body())
                .build();
        return MulticastMessage.builder()
                .setNotification(notification)
                .putAllData(pushNotification.data())
                .addAllTokens(tokens)
                .build();
    }

    private PushResult getPushResult(final BatchResponse batchResponse, final List<String> tokens) {
        final int failureCount = batchResponse.getFailureCount();
        if (failureCount == 0) {
            return PushResult.SUCCESS;
        }

        final List<SendResponse> responses = batchResponse.getResponses();
        final List<String> failedTokens = new ArrayList<>();
        for (int i = 0; i < responses.size(); i++) {
            if (responses.get(i).isSuccessful()) {
                continue;
            }
            failedTokens.add(tokens.get(i));
        }
        return new PushResult(failureCount, failedTokens);
    }
}
