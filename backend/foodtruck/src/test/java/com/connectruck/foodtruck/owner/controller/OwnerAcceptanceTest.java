package com.connectruck.foodtruck.owner.controller;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpStatus.OK;

import com.connectruck.foodtruck.common.testbase.AcceptanceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.order.domain.OrderInfo;
import com.connectruck.foodtruck.order.domain.OrderStatus;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.Role;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OwnerAcceptanceTest extends AcceptanceTestBase {

    private static final String BASE_URI = "/api/owner";

    private String token;
    private Event event;
    private Truck owningTruck;

    @BeforeEach
    void setUp() {
        // 사장님 계정 생성, 로그인
        final String username = "test";
        final String password = "test1234!";
        Account owner = dataSetup.saveAccount(Account.ofNew(username, password, "01000000000", Role.OWNER));
        token = loginAndGetToken(username, password);

        // 소유 푸드트럭 1개 저장
        event = dataSetup.saveEvent(밤도깨비_야시장.create());
        owningTruck = dataSetup.saveTruck(event, owner.getId());
    }

    @DisplayName("소유 푸드트럭의 정보를 사장님 id로 조회한다.")
    @Test
    void findMyTruck() {
        // given
        // 해당 계정의 소유 아닌 푸드트럭 1개 존재
        dataSetup.saveTruck(event);

        // when
        final ValidatableResponse response = getWithToken(BASE_URI + "/trucks/my", token);

        // then
        response.statusCode(OK.value())
                .body("id", equalTo(owningTruck.getId().intValue()))
                .body("name", equalTo(owningTruck.getName()));
    }

    @DisplayName("소유 푸드트럭의 상태별 주문 목록 조회")
    @Nested
    class findMyOrders {

        private static final String URI = BASE_URI + "/trucks/my/orders";

        @DisplayName("모든 주문을 최신순으로 정렬하여 특정 페이지를 조회한다.")
        @Test
        void all_perPage() {
            // given
            final Menu savedMenu = dataSetup.saveMenu(owningTruck);
            final OrderInfo expected = dataSetup.saveOrderInfo(owningTruck, savedMenu);
            dataSetup.saveOrderInfo(owningTruck, savedMenu);
            dataSetup.saveOrderInfo(owningTruck, savedMenu);

            // when
            final int page = 1;
            final int size = 2;
            final String params = String.format("?page=%d&size=%d", page, size);
            final ValidatableResponse response = getWithToken(URI + params, token);

            // then
            response.statusCode(OK.value())
                    .body("page.size", equalTo(size))
                    .body("page.currentPage", equalTo(page))
                    .body("page.hasNext", equalTo(false))
                    .body("orders", hasSize(1))
                    .body("orders.id", contains(expected.getId().intValue()));
        }

        @DisplayName("특정 상태의 주문을 조회한다.")
        @Test
        void byStatus_perPage() {
            // given
            final Menu savedMenu = dataSetup.saveMenu(owningTruck);
            final OrderInfo expected = dataSetup.saveOrderInfo(owningTruck, savedMenu);

            // 완료 상태의 주문 존재
            dataSetup.saveOrderInfo(owningTruck, savedMenu, OrderStatus.COMPLETE);

            // when
            final String status = OrderStatus.CREATED.name().toLowerCase();
            final int page = 0;
            final int size = 2;
            final String params = String.format("?status=%s&page=%d&size=%d", status, page, size);
            final ValidatableResponse response = getWithToken(URI + params, token);

            // then
            response.statusCode(OK.value())
                    .body("page.size", equalTo(size))
                    .body("page.currentPage", equalTo(page))
                    .body("page.hasNext", equalTo(false))
                    .body("orders", hasSize(1))
                    .body("orders.id", contains(expected.getId().intValue()));
        }

        @DisplayName("페이지와 사이즈를 지정하지 않으면 첫 20개를 조회한다.")
        @Test
        void findFirst20_withNoPageAndSize() {
            // given & when
            final ValidatableResponse response = getWithToken(URI, token);

            // then
            response.statusCode(OK.value())
                    .body("page.size", equalTo(20))
                    .body("page.currentPage", equalTo(0));
        }
    }
}
