package com.connectruck.foodtruck.notification.service;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.notification.domain.PushNotification;
import com.connectruck.foodtruck.notification.domain.PushResult;
import com.connectruck.foodtruck.notification.domain.PushSender;
import com.connectruck.foodtruck.notification.domain.PushSubscription;
import com.connectruck.foodtruck.notification.domain.PushSubscriptionRepository;
import com.connectruck.foodtruck.notification.dto.PushSubscribeRequest;
import com.connectruck.foodtruck.order.domain.OrderStatus;
import com.connectruck.foodtruck.order.message.OrderMessage;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.user.domain.Account;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

class PushNotificationServiceTest extends ServiceTestBase {

    @Autowired
    private PushNotificationService pushNotificationService;
    @MockBean
    private PushSender pushSender;
    @SpyBean
    private PushSubscriptionRepository pushSubscriptionRepository;

    @DisplayName("소유 푸드트럭 주문 push 알림 구독")
    @Nested
    class subscribeOrders {

        @DisplayName("소유한 푸드트럭의 주문 알림을 구독한다.")
        @Test
        void success() {
            // given
            final Event event = dataSetup.saveEvent(밤도깨비_야시장.create());
            final Account owner = dataSetup.saveOwnerAccount();
            dataSetup.saveTruck(event, owner.getId());

            // when & then
            final PushSubscribeRequest request = new PushSubscribeRequest("fake.token");
            assertThatNoException()
                    .isThrownBy(() -> pushNotificationService.subscribeOrders(request, owner.getId()));
        }

        @DisplayName("소유 푸드트럭에 대해 이미 구독된 기기이면 새 구독 정보를 생성하지 않는다.")
        @Test
        void success_whenAlreadySubscribed() {
            // given
            final Event event = dataSetup.saveEvent(밤도깨비_야시장.create());
            final Account owner = dataSetup.saveOwnerAccount();
            dataSetup.saveTruck(event, owner.getId());

            final PushSubscribeRequest request = new PushSubscribeRequest("fake.token");
            pushNotificationService.subscribeOrders(request, owner.getId());

            // when & then
            assertThatNoException()
                    .isThrownBy(() -> pushNotificationService.subscribeOrders(request, owner.getId()));
        }

        @DisplayName("소유한 푸드트럭이 없으면 예외가 발생한다.")
        @Test
        void throwsException_whenNoOwningTruck() {
            // given
            final Account ownerNotOwningTruck = dataSetup.saveOwnerAccount();

            // when & then
            final PushSubscribeRequest request = new PushSubscribeRequest("fake.token");
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> pushNotificationService.subscribeOrders(request, ownerNotOwningTruck.getId()))
                    .withMessageContainingAll("소유한 푸드트럭", "존재하지 않습니다.");
        }
    }

    @DisplayName("사장님 주문 알림 발송")
    @Nested
    class notifyOrderToOwner {

        @Test
        @DisplayName("해당 푸드트럭의 주문 알림 구독자에게 알림을 발송한다.")
        void success() {
            // given
            // 구독 정보 저장
            final Event event = dataSetup.saveEvent(밤도깨비_야시장.create());
            final Truck truck = dataSetup.saveTruck(event);
            final PushSubscription pushSubscription = dataSetup.savePushSubscription("fake.token", truck);

            // 주문 정보
            final long orderId = 0L;
            final OrderStatus status = OrderStatus.CREATED;
            final PushNotification expectedNotification = PushNotification.ofOrder(orderId, status);

            // 해당 주문 정보로 알림 발송 요청 시 성공
            Mockito.when(pushSender.send(
                    eq(expectedNotification),
                    argThat(list -> list.contains(pushSubscription))
            )).thenReturn(PushResult.SUCCESS);

            // when & then
            final OrderMessage orderMessage = new OrderMessage(orderId, status, truck.getId());
            assertThatNoException()
                    .isThrownBy(() -> pushNotificationService.notifyOrderToOwner(orderMessage));
        }

        @Test
        @DisplayName("발송에 실패한 토큰이 있을 경우 해당 구독 정보를 삭제한다.")
        void deleteSubscription_whenTokenFailed() {
            // given
            // 구독 정보 저장
            final Event event = dataSetup.saveEvent(밤도깨비_야시장.create());
            final Truck truck = dataSetup.saveTruck(event);

            // 알림 발송 요청 시 실패
            final String failedToken = "failed.token";
            dataSetup.savePushSubscription(failedToken, truck);
            Mockito.when(pushSender.send(any(), any()))
                    .thenReturn(new PushResult(1, List.of(failedToken)));

            // when
            final OrderMessage orderMessage = new OrderMessage(0L, OrderStatus.CREATED, truck.getId());
            pushNotificationService.notifyOrderToOwner(orderMessage);

            // then
            verify(pushSubscriptionRepository).deleteByTokenAndTruckId(failedToken, truck.getId());
        }
    }
}
