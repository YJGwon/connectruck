package com.connectruck.foodtruck.order.domain;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.connectruck.foodtruck.common.testbase.RepositoryTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.truck.domain.Truck;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderInfoRepositoryTest extends RepositoryTestBase {

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    private Truck savedTruck;
    private Menu savedMenu;

    @BeforeEach
    void setUp() {
        final Event event = dataSetup.saveEvent(밤도깨비_야시장.create());
        savedTruck = dataSetup.saveTruck(event);
        savedMenu = dataSetup.saveMenu(savedTruck);
    }

    @DisplayName("주문 정보 저장")
    @Nested
    class save {

        @DisplayName("생성 일시를 함께 저장한다.")
        @Test
        void withCreatedAt() {
            // given
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

    @DisplayName("특정 주문 정보를 id로 조회한다.")
    @Test
    void findById() {
        // given
        final OrderInfo expected = dataSetup.saveOrderInfo(savedTruck, savedMenu);
        final List<OrderLine> expectedOrderLines = expected.getOrderLines();

        // when
        final Optional<OrderInfo> found = orderInfoRepository.findById(expected.getId());

        // then
        final OrderInfo actual = found.get();
        assertAll(
                () -> assertThat(actual).isEqualTo(expected),
                () -> assertThat(actual.getOrderLines().containsAll(expectedOrderLines)).isTrue()
        );
    }
}
