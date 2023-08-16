package com.connectruck.foodtruck.notification.domain.push;

import static org.assertj.core.api.Assertions.assertThat;

import com.connectruck.foodtruck.common.fixture.data.EventFixture;
import com.connectruck.foodtruck.common.testbase.RepositoryTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.truck.domain.Truck;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PushSubscriptionRepositoryTest extends RepositoryTestBase {

    @Autowired
    private PushSubscriptionRepository pushSubscriptionRepository;

    @DisplayName("푸시 알림 구독 정보를 저장한다.")
    @Test
    void save() {
        // given
        final Event savedEvent = dataSetup.saveEvent(EventFixture.밤도깨비_야시장.create());
        final Truck savedTruck = dataSetup.saveTruck(savedEvent);
        final PushSubscription pushSubscription = PushSubscription.ofNew("fake.token", savedTruck.getId());

        // when
        final PushSubscription saved = pushSubscriptionRepository.save(pushSubscription);

        // then
        assertThat(saved.getId()).isNotNull();
    }
}
