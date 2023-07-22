package com.connectruck.foodtruck.notification.service;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.order.domain.OrderInfo;
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

        @DisplayName("구독할 때, 마지막으로 수신한 이벤트 이후에 이벤트가 더 있었다면 마저 수신한다.")
        @Test
        void withLastEventId() {
            // given
            final Event event = dataSetup.saveEvent(밤도깨비_야시장.create());
            final Account owner = dataSetup.saveOwnerAccount();
            dataSetup.saveTruck(event, owner.getId());

            // when & then
            assertThatNoException()
                    .isThrownBy(() -> notificationService.subscribeOrders(owner.getId(), "fakeId"));
        }
    }

    @DisplayName("주문 생성 알림.")
    @Nested
    class notifyOrderCreated {

        @DisplayName("해당 푸드트럭의 주문 알림 구독자에게 주문 생성 알림을 발송한다.")
        @Test
        void success() {
            // given
            final Event event = dataSetup.saveEvent(밤도깨비_야시장.create());
            final Account owner = dataSetup.saveOwnerAccount();
            final Truck savedTruck = dataSetup.saveTruck(event, owner.getId());
            notificationService.subscribeOrders(owner.getId(), "");

            final Menu savedMenu = dataSetup.saveMenu(savedTruck);
            final OrderInfo createdOrder = dataSetup.saveOrderInfo(savedTruck, savedMenu);

            // when & then
            assertThatNoException()
                    .isThrownBy(() ->
                            notificationService.notifyOrderCreated(createdOrder.getTruckId(), createdOrder.getId())
                    );
        }
    }
}
