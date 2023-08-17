package com.connectruck.foodtruck.notification.service;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.notification.dto.PushSubscribeRequest;
import com.connectruck.foodtruck.user.domain.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PushNotificationServiceTest extends ServiceTestBase {

    @Autowired
    private PushNotificationService pushNotificationService;

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
}
