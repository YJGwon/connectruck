package com.connectruck.foodtruck.order.domain;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.data.domain.Sort.Direction.DESC;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

class OrderInfoRepositoryTest extends RepositoryTestBase {

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    private Event savedEvent;
    private Truck savedTruck;
    private Menu savedMenu;

    @BeforeEach
    void setUp() {
        savedEvent = dataSetup.saveEvent(밤도깨비_야시장.create());
        savedTruck = dataSetup.saveTruck(savedEvent);
        savedMenu = dataSetup.saveMenu(savedTruck);
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
                () -> assertThat(actual.getOrderLines()).containsAll(expectedOrderLines)
        );
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

    @DisplayName("푸드트럭 주문 목록 조회")
    @Nested
    class findByTruckId {

        @DisplayName("최신 순으로 정렬해 페이지 단위로 조회한다.")
        @Test
        void latest_perPage() {
            // given
            dataSetup.saveOrderInfo(savedTruck, savedMenu);
            final OrderInfo expected2 = dataSetup.saveOrderInfo(savedTruck, savedMenu);
            final OrderInfo expected1 = dataSetup.saveOrderInfo(savedTruck, savedMenu);

            // when
            final Sort latest = Sort.by(DESC, "createdAt");
            final PageRequest pageRequest = PageRequest.of(0, 2, latest);
            final Page<OrderInfo> found = orderInfoRepository.findByTruckId(savedTruck.getId(), pageRequest);

            // then
            assertThat(found.getContent()).containsExactly(expected1, expected2);
        }

        @DisplayName("특정 푸드트럭의 주문만 조회한다.")
        @Test
        void notContainingOtherTruck() {
            // given
            final OrderInfo expected = dataSetup.saveOrderInfo(savedTruck, savedMenu);

            // 다른 트럭 대상 주문 1건 존재
            final Truck otherTruck = dataSetup.saveTruck(savedEvent);
            final Menu menuOfOtherTruck = dataSetup.saveMenu(savedTruck);
            dataSetup.saveOrderInfo(otherTruck, menuOfOtherTruck);

            // when
            final PageRequest pageRequest = PageRequest.of(0, 2);
            final Page<OrderInfo> found = orderInfoRepository.findByTruckId(savedTruck.getId(), pageRequest);

            // then
            assertThat(found.getContent()).containsExactly(expected);
        }
    }

    @DisplayName("푸드트럭 상태별 주문 목록 조회")
    @Nested
    class findByTruckIdAndStatus {

        @DisplayName("특정 푸드트럭의 특정 상태의 주문만 조회한다.")
        @Test
        void notContainingOtherStatus() {
            // given
            final OrderInfo expected = dataSetup.saveOrderInfo(savedTruck, savedMenu);

            // 완료 상태의 주문 1건 존재
            dataSetup.saveOrderInfo(savedTruck, savedMenu, OrderStatus.COMPLETE);

            // when
            final PageRequest pageRequest = PageRequest.of(0, 2);
            final Page<OrderInfo> found = orderInfoRepository.findByTruckIdAndStatus(
                    savedTruck.getId(), OrderStatus.CREATED, pageRequest
            );

            // then
            assertThat(found.getContent()).containsExactly(expected);
        }
    }
}
