package com.connectruck.foodtruck.notification.service;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.user.domain.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class NotificationServiceTest extends ServiceTestBase {

    @Autowired
    private NotificationService notificationService;

    @DisplayName("소유 푸드트럭 주문 알림 구독")
    @Nested
    class subscribeOrders {

        @DisplayName("소유한 푸드트럭의 주문 알림을 구독한다.")
        @Test
        void withOutLastEventId() {
            // given
            final Event event = dataSetup.saveEvent(밤도깨비_야시장.create());
            final Account owner = dataSetup.saveOwnerAccount();
            dataSetup.saveTruck(event, owner.getId());

            // when & then
            assertThatNoException()
                    .isThrownBy(() -> notificationService.subscribeOrders(owner.getId(), ""));
        }

        @DisplayName("소유한 푸드트럭이 없으면 예외가 발생한다.")
        @Test
        void throwsException_whenNoOwningTruck() {
            // given
            final Account ownerNotOwningTruck = dataSetup.saveOwnerAccount();

            // when & then
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> notificationService.subscribeOrders(ownerNotOwningTruck.getId(), ""))
                    .withMessageContainingAll("소유한 푸드트럭", "존재하지 않습니다.");
        }

        @DisplayName("구독할 때, 마지막으로 수신한 이벤트 이후에 이벤트가 더 있었다면 마저 수신한다.")
        @Test
        void withLastEventId() {
            // given
            final Event event = dataSetup.saveEvent(밤도깨비_야시장.create());
            final Account owner = dataSetup.saveOwnerAccount();
            final Truck truck = dataSetup.saveTruck(event, owner.getId());

            // when & then
            final String lastEventId = truck.getId() + "_" + System.currentTimeMillis();
            assertThatNoException()
                    .isThrownBy(() -> notificationService.subscribeOrders(owner.getId(), lastEventId));
        }
    }
}
