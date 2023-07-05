package com.connectruck.foodtruck.order.service;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThat;

import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.order.dto.OrderMenuRequest;
import com.connectruck.foodtruck.order.dto.OrderRequest;
import com.connectruck.foodtruck.truck.domain.Participation;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTestBase {

    @Autowired
    private OrderService orderService;

    @DisplayName("주문 생성")
    @Nested
    class create {

        @DisplayName("주문을 생성한다.")
        @Test
        void success() {
            // given
            final Event event = 밤도깨비_야시장.create();
            dataSetup.saveEvent(event);

            final Participation savedParticipation = dataSetup.saveParticipation(event);
            final Menu savedMenu = dataSetup.saveMenu(savedParticipation);

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
    }
}
