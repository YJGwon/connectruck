package com.connectruck.foodtruck.owner.domain;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThat;

import com.connectruck.foodtruck.common.testbase.RepositoryTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.user.domain.Account;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OwnerTruckRepositoryTest extends RepositoryTestBase {

    @Autowired
    private OwnerTruckRepository ownerTruckRepository;

    @DisplayName("사장님 계정 id로 푸드트럭 정보를 조회한다.")
    @Test
    void findByOwnerId() {
        // given
        final Event event = 밤도깨비_야시장.create();
        dataSetup.saveEvent(event);

        final Account owner = dataSetup.saveOwnerAccount();
        final Truck expected = dataSetup.saveTruck(event, owner.getId());

        // 해당 계정의 소유 아닌 푸드트럭 1개 존재
        dataSetup.saveTruck(event);

        // when
        final Optional<Truck> found = ownerTruckRepository.findByOwnerId(owner.getId());

        // then
        assertThat(found.get()).isEqualTo(expected);
    }
}
