package com.connectruck.foodtruck.menu.service;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.connectruck.foodtruck.common.exception.ClientException;
import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.menu.dto.MenuDescriptionRequest;
import com.connectruck.foodtruck.menu.dto.MenuSoldOutRequest;
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

    @DisplayName("푸드트럭 id로 메뉴 목록 조회")
    @Nested
    class findByTruckId {

        @DisplayName("할 수 있다.")
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

    @DisplayName("사장님 id로 소유 푸드트럭 메뉴 목록 조회")
    @Nested
    class findByOwnerId {

        @DisplayName("할 수 있다.")
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

        @DisplayName("할 때, 소유한 푸드트럭이 없으면 예외가 발생한다.")
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

    @DisplayName("소유 푸드트럭 메뉴 설명 수정")
    @Nested
    class updateDetail {

        @DisplayName("할 수 있다.")
        @Test
        void success() {
            // given
            final Menu savedMenu = dataSetup.saveMenu(savedTruck);

            // when & then
            final MenuDescriptionRequest request = new MenuDescriptionRequest("some description");
            assertThatNoException()
                    .isThrownBy(() -> menuService.updateDescription(request, savedMenu.getId(), owner.getId()));
        }

        @DisplayName("할 때, 존재하지 않는 메뉴면 예외가 발생한다.")
        @Test
        void throwException_ifMenuNotFound() {
            // given
            final Long fakeId = 0L;

            // when & then
            final MenuDescriptionRequest request = new MenuDescriptionRequest("some description");
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> menuService.updateDescription(request, fakeId, owner.getId()))
                    .withMessageContainingAll("메뉴", "존재하지 않습니다.");
        }

        @DisplayName("할 때, 소유한 푸드트럭이 없으면 예외가 발생한다.")
        @Test
        void throwsException_whenNoOwningTruck() {
            // given
            final Menu savedMenu = dataSetup.saveMenu(savedTruck);
            final Account ownerNotOwningTruck = dataSetup.saveOwnerAccount();

            // when & then
            final MenuDescriptionRequest request = new MenuDescriptionRequest("some description");
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> menuService.updateDescription(
                            request, savedMenu.getId(), ownerNotOwningTruck.getId()
                    ))
                    .withMessageContainingAll("소유한 푸드트럭", "존재하지 않습니다.");
        }

        @DisplayName("할 때, 소유한 푸드트럭의 메뉴가 아니면 예외가 발생한다.")
        @Test
        void throwsException_whenNotOwnerOfMenu() {
            // given
            final Truck otherTruck = dataSetup.saveTruck(savedEvent);
            final Menu menuOfOtherTruck = dataSetup.saveMenu(otherTruck);

            // when & then
            final MenuDescriptionRequest request = new MenuDescriptionRequest("some description");
            assertThatExceptionOfType(ClientException.class)
                    .isThrownBy(() -> menuService.updateDescription(request, menuOfOtherTruck.getId(), owner.getId()))
                    .withMessageContaining("소유하지 않은 푸드트럭의 메뉴");
        }
    }

    @DisplayName("소유 푸드트럭 품절 상태 수정")
    @Nested
    class updateSoldOut {

        @DisplayName("할 수 있다.")
        @Test
        void success() {
            // given
            final Menu savedMenu = dataSetup.saveMenu(savedTruck);

            // when & then
            final MenuSoldOutRequest request = new MenuSoldOutRequest(true);
            assertThatNoException()
                    .isThrownBy(() -> menuService.updateSoldOut(request, savedMenu.getId(), owner.getId()));
        }

        @DisplayName("할 때, 존재하지 않는 메뉴면 예외가 발생한다.")
        @Test
        void throwException_ifMenuNotFound() {
            // given
            final Long fakeId = 0L;

            // when & then
            final MenuSoldOutRequest request = new MenuSoldOutRequest(true);
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> menuService.updateSoldOut(request, fakeId, owner.getId()))
                    .withMessageContainingAll("메뉴", "존재하지 않습니다.");
        }

        @DisplayName("할 때, 소유한 푸드트럭이 없으면 예외가 발생한다.")
        @Test
        void throwsException_whenNoOwningTruck() {
            // given
            final Menu savedMenu = dataSetup.saveMenu(savedTruck);
            final Account ownerNotOwningTruck = dataSetup.saveOwnerAccount();

            // when & then
            final MenuSoldOutRequest request = new MenuSoldOutRequest(true);
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> menuService.updateSoldOut(
                            request, savedMenu.getId(), ownerNotOwningTruck.getId()
                    ))
                    .withMessageContainingAll("소유한 푸드트럭", "존재하지 않습니다.");
        }

        @DisplayName("할 때, 소유한 푸드트럭의 메뉴가 아니면 예외가 발생한다.")
        @Test
        void throwsException_whenNotOwnerOfMenu() {
            // given
            final Truck otherTruck = dataSetup.saveTruck(savedEvent);
            final Menu menuOfOtherTruck = dataSetup.saveMenu(otherTruck);

            // when & then
            final MenuSoldOutRequest request = new MenuSoldOutRequest(true);
            assertThatExceptionOfType(ClientException.class)
                    .isThrownBy(() -> menuService.updateSoldOut(request, menuOfOtherTruck.getId(), owner.getId()))
                    .withMessageContaining("소유하지 않은 푸드트럭의 메뉴");
        }
    }
}
