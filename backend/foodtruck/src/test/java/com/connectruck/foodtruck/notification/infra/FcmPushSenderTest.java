package com.connectruck.foodtruck.notification.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.connectruck.foodtruck.notification.domain.PushNotification;
import com.connectruck.foodtruck.notification.domain.PushResult;
import com.connectruck.foodtruck.notification.domain.PushSubscription;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FcmPushSenderTest {

    private final FirebaseMessaging firebaseMessaging = mock(FirebaseMessaging.class);
    private final FcmPushSender fcmPushSender = new FcmPushSender(firebaseMessaging);

    @DisplayName("FCM push를 전송한 후 전송 결과를 반환한다.")
    @Test
    void send() throws FirebaseMessagingException {
        // given
        final BatchResponse mockedResponse = mock(BatchResponse.class);
        when(firebaseMessaging.sendEachForMulticast(any(MulticastMessage.class)))
                .thenReturn(mockedResponse);
        when(mockedResponse.getFailureCount())
                .thenReturn(0);

        // when
        final PushNotification pushNotification = new PushNotification("hi", "you've got a good news", Map.of());
        final List<PushSubscription> pushSubscriptions = List.of(PushSubscription.ofNew("fake.token", 0L));
        final PushResult pushResult = fcmPushSender.send(pushNotification, pushSubscriptions);

        // then
        assertThat(pushResult).isEqualTo(PushResult.SUCCESS);
    }
}
