package com.connectruck.foodtruck.order.service;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.event.service.EventService;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.order.domain.OrderInfo;
import com.connectruck.foodtruck.order.dto.OrderLineRequest;
import com.connectruck.foodtruck.order.dto.OrderRequest;
import com.connectruck.foodtruck.order.exception.OrderCreationException;
import com.connectruck.foodtruck.truck.domain.Truck;
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

    @DisplayName("주문 생성")
    @Nested
    class create {

        private Event event;
        private Truck savedTruck;
        private Menu savedMenu;

        @BeforeEach
        void setUp() {
            event = dataSetup.saveEvent(밤도깨비_야시장.create());
            setEventClosed(false);

            savedTruck = dataSetup.saveTruck(event);
            savedMenu = dataSetup.saveMenu(savedTruck);
        }

        @DisplayName("주문을 생성한다.")
        @Test
        void success() {
            // given
            final OrderRequest request = new OrderRequest(
                    savedTruck.getId(),
                    "01000000000",
                    List.of(new OrderLineRequest(savedMenu.getId(), 2))
            );

            // when
            final Long id = orderService.create(request);

            // then
            assertThat(id).isNotNull();
        }

        @DisplayName("행사가 열려있지 않으면 예외가 발생한다.")
        @Test
        void throwsException_whenEventIsClosed() {
            // given
            setEventClosed(true);

            final OrderRequest request = new OrderRequest(
                    savedTruck.getId(),
                    "01000000000",
                    List.of(new OrderLineRequest(savedMenu.getId(), 2))
            );

            // when & then
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

            final OrderRequest request = new OrderRequest(
                    savedTruck.getId(),
                    "01000000000",
                    List.of(new OrderLineRequest(savedMenu.getId(), 2),
                            new OrderLineRequest(menuOfOtherTruck.getId(), 1))
            );

            // when & then
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
}
