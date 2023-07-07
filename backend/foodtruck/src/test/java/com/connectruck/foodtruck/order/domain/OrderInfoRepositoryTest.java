package com.connectruck.foodtruck.order.domain;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThat;

import com.connectruck.foodtruck.common.testbase.RepositoryTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.truck.domain.Truck;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderInfoRepositoryTest extends RepositoryTestBase {

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @DisplayName("주문 정보 저장")
    @Nested
    class save {

        @DisplayName("생성 일시를 함께 저장한다.")
        @Test
        void withCreatedAt() {
            // given
            final Event event = 밤도깨비_야시장.create();
            dataSetup.saveEvent(event);

            final Truck savedTruck = dataSetup.saveParticipation(event);
            final OrderInfo orderInfo = OrderInfo.ofNew(savedTruck.getId(), "01000000000");

            // when
            orderInfoRepository.save(orderInfo);

            // then
            assertThat(orderInfo.getCreatedAt()).isNotNull();
        }

        @DisplayName("주문 메뉴 내역을 함께 저장한다.")
        @Test
        void withOrderLines() {
            // given
            final Event event = 밤도깨비_야시장.create();
            dataSetup.saveEvent(event);

            final Truck savedTruck = dataSetup.saveParticipation(event);
            final Menu savedMenu = dataSetup.saveMenu(savedTruck);

            final OrderInfo orderInfo = OrderInfo.ofNew(savedTruck.getId(), "01000000000");
            final OrderLine orderLine = OrderLine.ofNew(
                    savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(), 1, orderInfo
            );
            orderInfo.changeOrderLine(List.of(orderLine));

            // when
            orderInfoRepository.save(orderInfo);

            // then
            assertThat(orderLine.getId()).isNotNull();
        }
    }
}
