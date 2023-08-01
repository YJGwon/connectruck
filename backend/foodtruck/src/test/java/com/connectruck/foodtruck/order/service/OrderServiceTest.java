package com.connectruck.foodtruck.order.service;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static com.connectruck.foodtruck.common.fixture.data.EventFixture.서울FC_경기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.connectruck.foodtruck.common.dto.PageResponse;
import com.connectruck.foodtruck.common.exception.ClientException;
import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.order.domain.OrderInfo;
import com.connectruck.foodtruck.order.domain.OrderLine;
import com.connectruck.foodtruck.order.domain.OrderStatus;
import com.connectruck.foodtruck.order.dto.OrderDetailResponse;
import com.connectruck.foodtruck.order.dto.OrderLineRequest;
import com.connectruck.foodtruck.order.dto.OrderLineResponse;
import com.connectruck.foodtruck.order.dto.OrderRequest;
import com.connectruck.foodtruck.order.dto.OrderResponse;
import com.connectruck.foodtruck.order.dto.OrdererInfoRequest;
import com.connectruck.foodtruck.order.dto.OrdersResponse;
import com.connectruck.foodtruck.order.exception.NotOwnerOfOrderException;
import com.connectruck.foodtruck.order.exception.OrderCreationException;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.user.domain.Account;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTestBase {

    @Autowired
    private OrderService orderService;

    private Event savedEvent;
    private Account owner;
    private Truck savedTruck;
    private Menu savedMenu;

    @BeforeEach
    void setUp() {
        savedEvent = dataSetup.saveEvent(밤도깨비_야시장.create());
        owner = dataSetup.saveOwnerAccount();
        savedTruck = dataSetup.saveTruck(savedEvent, owner.getId());
        savedMenu = dataSetup.saveMenu(savedTruck);
    }

    @DisplayName("주문 생성")
    @Nested
    class create {

        @BeforeEach
        void setUp() {
            dataSetup.setEventOpen(savedEvent);
        }

        @DisplayName("주문을 생성한다.")
        @Test
        void success() {
            // given & when
            final OrderRequest request = new OrderRequest(
                    savedTruck.getId(),
                    "01000000000",
                    List.of(new OrderLineRequest(savedMenu.getId(), 2))
            );
            final Long id = orderService.create(request);

            // then
            assertThat(id).isNotNull();
        }

        @DisplayName("행사가 열려있지 않으면 예외가 발생한다.")
        @Test
        void throwsException_whenEventIsClosed() {
            // given
            final Event closedEvent = dataSetup.saveEvent(서울FC_경기.create());
            final Truck truckOfClosedEvent = dataSetup.saveTruck(closedEvent);
            final Menu menu = dataSetup.saveMenu(truckOfClosedEvent);

            // when & then
            final OrderRequest request = new OrderRequest(
                    truckOfClosedEvent.getId(),
                    "01000000000",
                    List.of(new OrderLineRequest(menu.getId(), 2))
            );
            assertThatExceptionOfType(OrderCreationException.class)
                    .isThrownBy(() -> orderService.create(request))
                    .withMessageContaining("운영 시간");
        }

        @DisplayName("존재하지 않는 푸드트럭에 주문하면 예외가 발생한다.")
        @Test
        void throwsException_whenTruckNotFound() {
            // given
            final Long fakeTruckId = 0L;

            // when & then
            final OrderRequest request = new OrderRequest(
                    fakeTruckId,
                    "01000000000",
                    List.of(new OrderLineRequest(savedMenu.getId(), 2))
            );
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> orderService.create(request))
                    .withMessageContaining("푸드트럭");
        }

        @DisplayName("존재하지 않는 메뉴를 주문하면 예외가 발생한다.")
        @Test
        void throwsException_whenMenuNotFound() {
            // given
            final Long fakeMenuId = 0L;

            // when & then
            final OrderRequest request = new OrderRequest(
                    savedTruck.getId(),
                    "01000000000",
                    List.of(new OrderLineRequest(fakeMenuId, 2))
            );
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> orderService.create(request))
                    .withMessageContaining("메뉴");
        }

        @DisplayName("해당 푸드트럭의 메뉴가 아닌 메뉴를 주문하면 예외가 발생한다.")
        @Test
        void throwsException_whenMenuOfOtherTruck() {
            // given
            final Truck otherTruck = dataSetup.saveTruck(savedEvent);
            final Menu menuOfOtherTruck = dataSetup.saveMenu(otherTruck);

            // when & then
            final OrderRequest request = new OrderRequest(
                    savedTruck.getId(),
                    "01000000000",
                    List.of(new OrderLineRequest(savedMenu.getId(), 2),
                            new OrderLineRequest(menuOfOtherTruck.getId(), 1))
            );
            assertThatExceptionOfType(OrderCreationException.class)
                    .isThrownBy(() -> orderService.create(request))
                    .withMessageContaining("다른 푸드트럭");
        }
    }

    @DisplayName("주문자 주문 상세 정보 조회")
    @Nested
    class findByIdAndOrdererInfo {

        @DisplayName("주문 상세 정보를 id와 주문자 정보로 조회한다.")
        @Test
        void success() {
            // given
            final OrderInfo expected = dataSetup.saveOrderInfo(savedTruck, savedMenu);
            final String expectedEventName = savedEvent.getName();
            final String expectedTruckName = savedTruck.getName();
            final List<Long> expectedOrderLineIds = expected.getOrderLines()
                    .stream()
                    .map(OrderLine::getId)
                    .toList();

            // when
            final OrdererInfoRequest request = new OrdererInfoRequest(expected.getPhone());
            final OrderDetailResponse response = orderService.findByIdAndOrdererInfo(expected.getId(), request);

            // then
            final List<Long> actualOrderLineIds = response.menus()
                    .stream()
                    .map(OrderLineResponse::id)
                    .toList();

            assertAll(
                    () -> assertThat(response.id()).isEqualTo(expected.getId()),
                    () -> assertThat(response.event().name()).isEqualTo(expectedEventName),
                    () -> assertThat(response.truck().name()).isEqualTo(expectedTruckName),
                    () -> assertThat(actualOrderLineIds).containsAll(expectedOrderLineIds)
            );
        }

        @DisplayName("id가 옳지 않으면 예외가 발생한다.")
        @Test
        void throwsException_whenWrongId() {
            // given
            final OrderInfo orderInfo = dataSetup.saveOrderInfo(savedTruck, savedMenu);
            final Long fakeId = 0L;

            // when & then
            final OrdererInfoRequest request = new OrdererInfoRequest(orderInfo.getPhone());
            assertThatExceptionOfType(ClientException.class)
                    .isThrownBy(() -> orderService.findByIdAndOrdererInfo(fakeId, request))
                    .withMessageContaining("잘못된 주문 정보");
        }

        @DisplayName("휴대폰 번호가 옳지 않으면 예외가 발생한다.")
        @Test
        void throwsException_whenWrongPhone() {
            // given
            final OrderInfo orderInfo = dataSetup.saveOrderInfo(savedTruck, savedMenu);
            final String fakePhone = "01099999999";

            // when & then
            final OrdererInfoRequest request = new OrdererInfoRequest(fakePhone);
            assertThatExceptionOfType(ClientException.class)
                    .isThrownBy(() -> orderService.findByIdAndOrdererInfo(orderInfo.getId(), request))
                    .withMessageContaining("잘못된 주문 정보");
        }
    }

    @DisplayName("사장님 소유 푸드트럭의 특정 주문 정보 조회")
    @Nested
    class findByIdAndOwnerId {

        @DisplayName("소유한 푸드트럭의 주문 정보를 id로 조회한다.")
        @Test
        void success() {
            // given
            final OrderInfo expected = dataSetup.saveOrderInfo(savedTruck, savedMenu);
            final List<Long> expectedOrderLineIds = expected.getOrderLines()
                    .stream()
                    .map(OrderLine::getId)
                    .toList();

            // when
            final OrderResponse response = orderService.findByIdAndOwnerId(expected.getId(), owner.getId());

            // then
            final List<Long> actualOrderLineIds = response.menus()
                    .stream()
                    .map(OrderLineResponse::id)
                    .toList();

            assertAll(
                    () -> assertThat(response.id()).isEqualTo(expected.getId()),
                    () -> assertThat(actualOrderLineIds).containsAll(expectedOrderLineIds)
            );
        }

        @DisplayName("소유하지 않은 푸드트럭의 주문 정보를 조회하면 예외가 발생한다.")
        @Test
        void throwsException_whenNotOwnerOfOrder() {
            // given
            final Truck otherTruck = dataSetup.saveTruck(savedEvent);
            final OrderInfo orderToOtherTruck = dataSetup.saveOrderInfo(otherTruck, savedMenu);

            // when & then
            assertThatExceptionOfType(NotOwnerOfOrderException.class)
                    .isThrownBy(() -> orderService.findByIdAndOwnerId(orderToOtherTruck.getId(), owner.getId()));
        }

        @DisplayName("해당하는 주문 정보가 존재하지 않으면 예외가 발생한다.")
        @Test
        void throwsException_whenOrderInfoNotFound() {
            // given
            final Long fakeId = 0L;

            // when & then
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> orderService.findByIdAndOwnerId(fakeId, owner.getId()))
                    .withMessageContaining("주문 정보", "존재하지 않습니다");
        }
    }

    @DisplayName("사장님 소유 푸드트럭의 상태별 주문 목록 조회")
    @Nested
    class findOrdersByOwnerIdAndStatus {

        @DisplayName("특정 주문 상태의 주문 목록을 최신순으로 정렬하여 page 단위로 조회한다.")
        @Test
        void latest_perPage() {
            // given
            dataSetup.saveOrderInfo(savedTruck, savedMenu);

            // 완료 상태의 주문 1건 존재
            dataSetup.saveOrderInfo(savedTruck, savedMenu, OrderStatus.COMPLETE);

            // when
            final int page = 0;
            final int size = 2;
            final OrdersResponse response = orderService.findOrdersByOwnerIdAndStatus(
                    owner.getId(), OrderStatus.CREATED.name(), page, size
            );

            // then
            assertThat(response.orders()).hasSize(1);
        }

        @DisplayName("상태가 ALL이면 모든 주문을 최신순으로 정렬하여 page 단위로 조회한다.")
        @Test
        void all_latest_perPage() {
            // given
            dataSetup.saveOrderInfo(savedTruck, savedMenu);
            dataSetup.saveOrderInfo(savedTruck, savedMenu);

            // 소유하지 않은 푸드트럭의 주문 1건 존재
            final Truck otherTruck = dataSetup.saveTruck(savedEvent);
            final Menu menuOfOtherTruck = dataSetup.saveMenu(otherTruck);
            dataSetup.saveOrderInfo(otherTruck, menuOfOtherTruck);

            // when
            final int page = 0;
            final int size = 2;
            final OrdersResponse response = orderService.findOrdersByOwnerIdAndStatus(
                    owner.getId(), OrderStatus.ALL.name(), page, size
            );

            // then
            assertAll(
                    () -> assertThat(response.orders()).hasSize(2),
                    () -> assertThat(response.page()).isEqualTo(new PageResponse(size, 1, page, false))
            );
        }

        @DisplayName("소유한 푸드트럭이 없으면 예외가 발생한다.")
        @Test
        void throwsException_whenNoOwningTruck() {
            // given
            final Account ownerNotOwningTruck = dataSetup.saveOwnerAccount();

            // when & then
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> orderService.findOrdersByOwnerIdAndStatus(
                            ownerNotOwningTruck.getId(), OrderStatus.CREATED.name(), 0, 1
                    ))
                    .withMessageContainingAll("소유한 푸드트럭", "존재하지 않습니다.");
        }
    }

    @DisplayName("주문 접수")
    @Nested
    class acceptOrder {

        @DisplayName("접수 대기 중인 주문을 접수한다.")
        @Test
        void success() {
            // given
            final OrderInfo createdOrder = dataSetup.saveOrderInfo(savedTruck, savedMenu);

            // when & then
            assertThatNoException()
                    .isThrownBy(() -> orderService.acceptOrder(createdOrder.getId(), owner.getId()));
        }

        @DisplayName("소유한 푸드트럭이 없으면 예외가 발생한다.")
        @Test
        void throwsException_whenNoOwningTruck() {
            // given
            final OrderInfo createdOrder = dataSetup.saveOrderInfo(savedTruck, savedMenu);
            final Account ownerNotOwningTruck = dataSetup.saveOwnerAccount();

            // when & then
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> orderService.acceptOrder(createdOrder.getId(), ownerNotOwningTruck.getId()))
                    .withMessageContainingAll("소유한 푸드트럭", "존재하지 않습니다.");
        }

        @DisplayName("소유하지 않은 푸드트럭의 주문을 접수하면 예외가 발생한다.")
        @Test
        void throwsException_whenNotOwnerOfOrder() {
            // given
            final Truck otherTruck = dataSetup.saveTruck(savedEvent);
            final OrderInfo orderToOtherTruck = dataSetup.saveOrderInfo(otherTruck, savedMenu);

            // when & then
            assertThatExceptionOfType(NotOwnerOfOrderException.class)
                    .isThrownBy(() -> orderService.acceptOrder(orderToOtherTruck.getId(), owner.getId()));
        }
    }

    @DisplayName("주문 조리 완료 처리")
    @Nested
    class finishCooking {

        @DisplayName("조리 중인 주문을 조리 완료 처리한다.")
        @Test
        void success() {
            // given
            final OrderInfo cookingOrder = dataSetup.saveOrderInfo(savedTruck, savedMenu, OrderStatus.COOKING);

            // when & then
            assertThatNoException()
                    .isThrownBy(() -> orderService.finishCooking(cookingOrder.getId(), owner.getId()));
        }

        @DisplayName("소유한 푸드트럭이 없으면 예외가 발생한다.")
        @Test
        void throwsException_whenNoOwningTruck() {
            // given
            final OrderInfo cookingOrder = dataSetup.saveOrderInfo(savedTruck, savedMenu, OrderStatus.COOKING);
            final Account ownerNotOwningTruck = dataSetup.saveOwnerAccount();

            // when & then
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> orderService.finishCooking(cookingOrder.getId(), ownerNotOwningTruck.getId()))
                    .withMessageContainingAll("소유한 푸드트럭", "존재하지 않습니다.");
        }

        @DisplayName("소유하지 않은 푸드트럭의 주문을 조리 완료 처리하면 예외가 발생한다.")
        @Test
        void throwsException_whenNotOwnerOfOrder() {
            // given
            final Truck otherTruck = dataSetup.saveTruck(savedEvent);
            final OrderInfo orderToOtherTruck = dataSetup.saveOrderInfo(otherTruck, savedMenu, OrderStatus.COOKING);

            // when & then
            assertThatExceptionOfType(NotOwnerOfOrderException.class)
                    .isThrownBy(() -> orderService.finishCooking(orderToOtherTruck.getId(), owner.getId()));
        }
    }

    @DisplayName("주문 픽업 완료 처리")
    @Nested
    class complete {

        @DisplayName("조리 완료된 주문을 픽업 완료 처리한다.")
        @Test
        void success() {
            // given
            final OrderInfo cookedOrder = dataSetup.saveOrderInfo(savedTruck, savedMenu, OrderStatus.COOKED);

            // when & then
            assertThatNoException()
                    .isThrownBy(() -> orderService.complete(cookedOrder.getId(), owner.getId()));
        }

        @DisplayName("소유한 푸드트럭이 없으면 예외가 발생한다.")
        @Test
        void throwsException_whenNoOwningTruck() {
            // given
            final OrderInfo cookedOrder = dataSetup.saveOrderInfo(savedTruck, savedMenu, OrderStatus.COOKED);
            final Account ownerNotOwningTruck = dataSetup.saveOwnerAccount();

            // when & then
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> orderService.complete(cookedOrder.getId(), ownerNotOwningTruck.getId()))
                    .withMessageContainingAll("소유한 푸드트럭", "존재하지 않습니다.");
        }

        @DisplayName("소유하지 않은 푸드트럭의 주문을 픽업 완료 처리하면 예외가 발생한다.")
        @Test
        void throwsException_whenNotOwnerOfOrder() {
            // given
            final Truck otherTruck = dataSetup.saveTruck(savedEvent);
            final OrderInfo orderToOtherTruck = dataSetup.saveOrderInfo(otherTruck, savedMenu, OrderStatus.COOKED);

            // when & then
            assertThatExceptionOfType(NotOwnerOfOrderException.class)
                    .isThrownBy(() -> orderService.complete(orderToOtherTruck.getId(), owner.getId()));
        }
    }

    @DisplayName("주문 취소 처리")
    @Nested
    class cancel {

        @DisplayName("진행중인 주문을 취소 처리한다.")
        @ParameterizedTest
        @ValueSource(strings = {"CREATED", "COOKING", "COOKED"})
        void success(final String inProgressStatus) {
            // given
            final OrderInfo inProgressOrder = dataSetup.saveOrderInfo(savedTruck, savedMenu,
                    OrderStatus.valueOf(inProgressStatus));

            // when & then
            assertThatNoException()
                    .isThrownBy(() -> orderService.cancel(inProgressOrder.getId(), owner.getId()));
        }


        @DisplayName("소유한 푸드트럭이 없으면 예외가 발생한다.")
        @Test
        void throwsException_whenNoOwningTruck() {
            // given
            final OrderInfo inProgressOrder = dataSetup.saveOrderInfo(savedTruck, savedMenu);
            final Account ownerNotOwningTruck = dataSetup.saveOwnerAccount();

            // when & then
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> orderService.cancel(inProgressOrder.getId(), ownerNotOwningTruck.getId()))
                    .withMessageContainingAll("소유한 푸드트럭", "존재하지 않습니다.");
        }

        @DisplayName("소유하지 않은 푸드트럭의 주문을 픽업 완료 처리하면 예외가 발생한다.")
        @Test
        void throwsException_whenNotOwnerOfOrder() {
            // given
            final Truck otherTruck = dataSetup.saveTruck(savedEvent);
            final OrderInfo orderToOtherTruck = dataSetup.saveOrderInfo(otherTruck, savedMenu, OrderStatus.CREATED);

            // when & then
            assertThatExceptionOfType(NotOwnerOfOrderException.class)
                    .isThrownBy(() -> orderService.cancel(orderToOtherTruck.getId(), owner.getId()));
        }
    }
}
