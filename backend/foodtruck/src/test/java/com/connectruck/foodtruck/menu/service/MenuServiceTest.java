package com.connectruck.foodtruck.menu.service;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.menu.dto.MenusResponse;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.user.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTestBase {

    @Autowired
    private MenuService menuService;

    private Event savedEvent;
    private Account owner;
    private Truck savedTruck;

    @BeforeEach
    void setUp() {
        savedEvent = dataSetup.saveEvent(밤도깨비_야시장.create());
        owner = dataSetup.saveOwnerAccount();
        savedTruck = dataSetup.saveTruck(savedEvent, owner.getId());
    }

    @DisplayName("푸드트럭 메뉴 목록 조회")
    @Nested
    class findByTruckId {

        @DisplayName("푸드트럭의 id로 메뉴 목록을 조회한다.")
        @Test
        void success() {
            // given
            dataSetup.saveMenu(savedTruck);
            dataSetup.saveMenu(savedTruck);

            // when
            final MenusResponse response = menuService.findByTruckId(savedTruck.getId());

            // then
            assertThat(response.menus()).hasSize(2);
        }
    }

    @DisplayName("사장님 소유 푸드트럭 메뉴 목록 조회")
    @Nested
    class findByOwnerId {

        @DisplayName("사장님 id로 소유한 푸드트럭의 메뉴 목록을 조회한다.")
        @Test
        void success() {
            // given
            dataSetup.saveMenu(savedTruck);
            dataSetup.saveMenu(savedTruck);

            // when
            final MenusResponse response = menuService.findByOwnerId(owner.getId());

            // then
            assertThat(response.menus()).hasSize(2);
        }

        @DisplayName("소유한 푸드트럭이 없으면 예외가 발생한다.")
        @Test
        void throwsException_whenNoOwningTruck() {
            // given
            final Account ownerNotOwningTruck = dataSetup.saveOwnerAccount();

            // when & then
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> menuService.findByOwnerId(ownerNotOwningTruck.getId()))
                    .withMessageContainingAll("소유한 푸드트럭", "존재하지 않습니다.");
        }
    }
}
