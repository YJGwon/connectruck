package com.connectruck.foodtruck.order.controller;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.connectruck.foodtruck.common.testbase.AcceptanceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.order.domain.OrderInfo;
import com.connectruck.foodtruck.order.domain.OrderStatus;
import com.connectruck.foodtruck.order.dto.OrderStatusRequest;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.user.domain.Account;
import com.connectruck.foodtruck.user.domain.Role;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class OwnerOrderAcceptanceTest extends AcceptanceTestBase {

    private static final String BASE_URI = "/api/owner/orders";

    private String token;
    private Truck owningTruck;
    private Menu savedMenu;

    @BeforeEach
    void setUp() {
        // 사장님 계정 생성, 로그인
        final String username = "test";
        final String password = "test1234!";
        final Account owner = dataSetup.saveAccount(Account.ofNew(username, password, "01000000000", Role.OWNER));
        token = loginAndGetToken(username, password);

        // 소유 푸드트럭 1개 저장, 메뉴 저장
        final Event event = dataSetup.saveEvent(밤도깨비_야시장.create());
        owningTruck = dataSetup.saveTruck(event, owner.getId());
        savedMenu = dataSetup.saveMenu(owningTruck);
    }

    @DisplayName("소유 푸드트럭의 상태별 주문 목록 조회")
    @Nested
    class findMyOrders {

        private static final String URI = BASE_URI + "/my";

        @DisplayName("모든 주문을 최신순으로 정렬하여 특정 페이지를 조회한다.")
        @Test
        void all_perPage() {
            // given
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
                    .body("orders.id", contains(expected.getId().intValue()))
                    .body("orders.status", contains("접수 대기"));
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

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeStatus() {
        // given
        final OrderInfo orderInfo = dataSetup.saveOrderInfo(owningTruck, savedMenu);

        // when
        final String uri = BASE_URI + String.format("/%d/status", orderInfo.getId());
        final OrderStatusRequest request = new OrderStatusRequest(OrderStatus.COOKING);
        final ValidatableResponse response = putWithToken(uri, request, token);

        // then
        response.statusCode(NO_CONTENT.value());
    }
}
