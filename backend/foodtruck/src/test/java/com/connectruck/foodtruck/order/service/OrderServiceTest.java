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
import com.connectruck.foodtruck.order.dto.OrderMenuRequest;
import com.connectruck.foodtruck.order.dto.OrderRequest;
import com.connectruck.foodtruck.order.exception.EventClosedException;
import com.connectruck.foodtruck.truck.domain.Participation;
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
        private Participation savedParticipation;
        private Menu savedMenu;

        @BeforeEach
        void setUp() {
            event = dataSetup.saveEvent(밤도깨비_야시장.create());
            setEventClosed(false);

            savedParticipation = dataSetup.saveParticipation(event);
            savedMenu = dataSetup.saveMenu(savedParticipation);
        }

        @DisplayName("주문을 생성한다.")
        @Test
        void success() {
            // given
            final OrderRequest request = new OrderRequest(
                    savedParticipation.getId(),
                    "01000000000",
                    List.of(new OrderMenuRequest(savedMenu.getId(), 2))
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
                    savedParticipation.getId(),
                    "01000000000",
                    List.of(new OrderMenuRequest(savedMenu.getId(), 2))
            );

            // when & then
            assertThatExceptionOfType(EventClosedException.class)
                    .isThrownBy(() -> orderService.create(request));
        }

        private void setEventClosed(final boolean value) {
            doReturn(value)
                    .when(eventService)
                    .isEventClosedAt(eq(event.getId()), any(LocalDateTime.class));
        }
    }
}
