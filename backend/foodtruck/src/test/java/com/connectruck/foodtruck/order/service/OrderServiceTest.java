package com.connectruck.foodtruck.order.service;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

import com.connectruck.foodtruck.common.dto.PageResponse;
import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.event.service.EventService;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.order.domain.OrderInfo;
import com.connectruck.foodtruck.order.domain.OrderLine;
import com.connectruck.foodtruck.order.domain.OrderStatus;
import com.connectruck.foodtruck.order.dto.OrderLineRequest;
import com.connectruck.foodtruck.order.dto.OrderLineResponse;
import com.connectruck.foodtruck.order.dto.OrderRequest;
import com.connectruck.foodtruck.order.dto.OrderResponse;
import com.connectruck.foodtruck.order.dto.OrdersResponse;
import com.connectruck.foodtruck.order.exception.OrderCreationException;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.user.domain.Account;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

class OrderServiceTest extends ServiceTestBase {

    @SpyBean
    private EventService eventService;

    @Autowired
    private OrderService orderService;


    private Event event;
    private Account owner;
    private Truck savedTruck;
    private Menu savedMenu;

    @BeforeEach
    void setUp() {
        event = dataSetup.saveEvent(밤도깨비_야시장.create());
        owner = dataSetup.saveOwnerAccount();
        savedTruck = dataSetup.saveTruck(event, owner.getId());
        savedMenu = dataSetup.saveMenu(savedTruck);
    }

    @DisplayName("주문 생성")
    @Nested
    class create {

        @BeforeEach
        void setUp() {
            setEventClosed(false);
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
            setEventClosed(true);

            // when & then
            final OrderRequest request = new OrderRequest(
                    savedTruck.getId(),
                    "01000000000",
                    List.of(new OrderLineRequest(savedMenu.getId(), 2))
            );
            assertThatExceptionOfType(OrderCreationException.class)
                    .isThrownBy(() -> orderService.create(request))
                    .withMessageContaining("운영 시간");
        }

        @DisplayName("해당 푸드트럭의 메뉴가 아닌 메뉴를 주문하면 예외가 발생한다.")
        @Test
        void throwsException_whenMenuOfOtherTruck() {
            // given
            final Truck otherTruck = dataSetup.saveTruck(event);
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

        private void setEventClosed(final boolean value) {
            doReturn(value)
                    .when(eventService)
                    .isEventClosedAt(eq(event.getId()), any(LocalDateTime.class));
        }
    }

    @DisplayName("특정 주문 정보 조회")
    @Nested
    class findById {

        @DisplayName("특정 주문 정보를 id로 조회한다.")
        @Test
        void success() {
            // given
            final OrderInfo expected = dataSetup.saveOrderInfo(savedTruck, savedMenu);
            final List<Long> expectedOrderLineIds = expected.getOrderLines()
                    .stream()
                    .map(OrderLine::getId)
                    .toList();

            // when
            final OrderResponse response = orderService.findById(expected.getId());

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

        @DisplayName("해당하는 주문 정보가 존재하지 않으면 예외가 발생한다.")
        @Test
        void throwsException_whenOrderInfoNotFound() {
            // given
            final Long fakeId = 0L;

            // when & then
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> orderService.findById(fakeId))
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
            final Truck otherTruck = dataSetup.saveTruck(event);
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
    }
}
