package com.connectruck.foodtruck.menu.domain;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThat;

import com.connectruck.foodtruck.common.testbase.RepositoryTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.truck.domain.Truck;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuRepositoryTest extends RepositoryTestBase {

    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("아이디로 특정 메뉴를 조회한다.")
    @Test
    void findById() {
        // given
        final Event event = 밤도깨비_야시장.create();
        dataSetup.saveEvent(event);

        final Truck savedTruck = dataSetup.saveParticipation(event);
        final Menu expected = dataSetup.saveMenu(savedTruck);

        // when
        final Optional<Menu> found = menuRepository.findById(expected.getId());

        // then
        assertThat(found.get()).isEqualTo(expected);
    }

    @DisplayName("푸드트럭의 메뉴 목록을 조회한다.")
    @Test
    void findByTruckId() {
        // given
        final Event event = 밤도깨비_야시장.create();
        dataSetup.saveEvent(event);

        final Truck savedTruck = dataSetup.saveParticipation(event);
        dataSetup.saveMenu(savedTruck);
        dataSetup.saveMenu(savedTruck);

        final Truck otherTruck = dataSetup.saveParticipation(event);
        dataSetup.saveMenu(otherTruck);

        // when
        final List<Menu> found = menuRepository.findByTruckId(savedTruck.getId());

        // when
        assertThat(found).hasSize(2);
    }
}
