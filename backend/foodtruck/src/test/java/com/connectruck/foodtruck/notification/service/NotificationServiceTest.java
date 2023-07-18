package com.connectruck.foodtruck.notification.service;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.user.domain.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class NotificationServiceTest extends ServiceTestBase {

    @Autowired
    private NotificationService notificationService;

    @DisplayName("소유한 푸드트럭의 주문 알림을 구독한다.")
    @Test
    void subscribeOrders() {
        // given
        final Event event = dataSetup.saveEvent(밤도깨비_야시장.create());
        final Account owner = dataSetup.saveOwnerAccount();
        dataSetup.saveTruck(event, owner.getId());

        // when & then
        assertThatNoException()
                .isThrownBy(() -> notificationService.subscribeOrders(owner.getId()));
    }
}
