package com.connectruck.foodtruck.owner.service;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.connectruck.foodtruck.common.dto.PageResponse;
import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.order.domain.OrderStatus;
import com.connectruck.foodtruck.owner.dto.OwnerOrdersResponse;
import com.connectruck.foodtruck.owner.dto.OwnerTruckResponse;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.user.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OwnerServiceTest extends ServiceTestBase {

    @Autowired
    private OwnerService ownerService;

    private Event event;
    private Account owner;
    private Truck owningTruck;

    @BeforeEach
    void setUp() {
        event = dataSetup.saveEvent(밤도깨비_야시장.create());
        owner = dataSetup.saveOwnerAccount();
        owningTruck = dataSetup.saveTruck(event, owner.getId());
    }

    @DisplayName("소유 푸드트럭 정보 조회")
    @Nested
    class findOwningTruck {

        @DisplayName("특정 푸드트럭의 정보를 사장님 계정 id로 조회한다.")
        @Test
        void success() {
            // given
            // 해당 계정의 소유 아닌 푸드트럭 1개 존재
            dataSetup.saveTruck(event);

            // when
            final OwnerTruckResponse response = ownerService.findOwningTruck(owner.getId());

            // then
            assertAll(
                    () -> assertThat(response.id()).isEqualTo(owningTruck.getId()),
                    () -> assertThat(response.name()).isEqualTo(owningTruck.getName())
            );
        }

        @DisplayName("해당하는 푸드트럭이 존재하지 않으면 예외가 발생한다.")
        @Test
        void throwsException_whenTruckNotFound() {
            // given
            final Account ownerNotHavingTruck = dataSetup.saveOwnerAccount();

            // when & then
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> ownerService.findOwningTruck(ownerNotHavingTruck.getId()))
                    .withMessageContainingAll("푸드트럭", "존재하지 않습니다");
        }
    }

    @DisplayName("소유 푸드트럭 상태별 주문 목록 조회")
    @Nested
    class findOrdersOfOwningTruckByStatus {

        @DisplayName("특정 주문 상태의 주문 목록을 최신순으로 정렬하여 page 단위로 조회한다.")
        @Test
        void latest_perPage() {
            // given
            final Menu savedMenu = dataSetup.saveMenu(owningTruck);
            dataSetup.saveOrderInfo(owningTruck, savedMenu);

            // 완료 상태의 주문 1건 존재
            dataSetup.saveOrderInfo(owningTruck, savedMenu, OrderStatus.COMPLETE);

            // when
            final int page = 0;
            final int size = 2;
            final OwnerOrdersResponse response = ownerService.findOrdersOfOwningTruckByStatus(
                    owner.getId(), OrderStatus.CREATED.name(), page, size
            );

            // then
            assertThat(response.orders()).hasSize(1);
        }

        @DisplayName("상태가 ALL이면 모든 주문을 최신순으로 정렬하여 page 단위로 조회한다.")
        @Test
        void all_latest_perPage() {
            // given
            final Menu savedMenu = dataSetup.saveMenu(owningTruck);
            dataSetup.saveOrderInfo(owningTruck, savedMenu);
            dataSetup.saveOrderInfo(owningTruck, savedMenu);

            // 소유하지 않은 푸드트럭의 주문 1건 존재
            final Truck otherTruck = dataSetup.saveTruck(event);
            final Menu menuOfOtherTruck = dataSetup.saveMenu(otherTruck);
            dataSetup.saveOrderInfo(otherTruck, menuOfOtherTruck);

            // when
            final int page = 0;
            final int size = 2;
            final OwnerOrdersResponse response = ownerService.findOrdersOfOwningTruckByStatus(
                    owner.getId(), OrderStatus.ALL.name(), page, size
            );

            // then
            assertAll(
                    () -> assertThat(response.orders()).hasSize(2),
                    () -> assertThat(response.page()).isEqualTo(new PageResponse(size, 1, page, false))
            );
        }

        @DisplayName("소유하는 푸드트럭이 존재하지 않으면 예외가 발생한다.")
        @Test
        void throwsException_whenTruckNotFound() {
            // given
            final Account ownerNotHavingTruck = dataSetup.saveOwnerAccount();

            // when & then
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> ownerService.findOrdersOfOwningTruckByStatus(
                            ownerNotHavingTruck.getId(), OrderStatus.ALL.name(), 0, 2
                    ))
                    .withMessageContainingAll("푸드트럭", "존재하지 않습니다");
        }
    }
}
