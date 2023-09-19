package com.connectruck.foodtruck.order;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.connectruck.foodtruck.common.testbase.AcceptanceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.order.domain.OrderInfo;
import com.connectruck.foodtruck.order.domain.OrderLine;
import com.connectruck.foodtruck.order.dto.OrderLineRequest;
import com.connectruck.foodtruck.order.dto.OrderRequest;
import com.connectruck.foodtruck.order.dto.OrdererInfoRequest;
import com.connectruck.foodtruck.truck.domain.Truck;
import io.restassured.response.ValidatableResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderAcceptanceTest extends AcceptanceTestBase {

    private static final String BASE_URI = "/api/orders";

    private Event event;
    private Truck savedTruck;
    private Menu savedMenu;

    @BeforeEach
    void setUp() {
        event = dataSetup.saveEvent(밤도깨비_야시장.create());
        savedTruck = dataSetup.saveTruck(event);
        savedMenu = dataSetup.saveMenu(savedTruck);
    }

    @DisplayName("한 푸드트럭에 대해 메뉴를 주문한다.")
    @Test
    void order() {
        // given
        dataSetup.setEventOpen(event);

        // when
        final OrderRequest request = new OrderRequest(
                savedTruck.getId(),
                "01000000000",
                List.of(new OrderLineRequest(savedMenu.getId(), 2))
        );
        final ValidatableResponse response = post(BASE_URI, request);

        // then
        response.statusCode(CREATED.value())
                .header(LOCATION, startsWith(BASE_URI));
    }

    @DisplayName("주문 상세정보를 주문 id와 주문자 정보로 조회한다.")
    @Test
    void findByIdAndOrdererInfo() {
        // given
        final OrderInfo expected = dataSetup.saveOrderInfo(savedTruck, savedMenu);
        final String expectedEventName = event.getName();
        final String expectedTruckName = savedTruck.getName();
        final String[] expectedMenuNames = expected.getOrderLines()
                .stream()
                .map(OrderLine::getMenuName)
                .toArray(String[]::new);

        // when
        final OrdererInfoRequest request = new OrdererInfoRequest(expected.getPhone());
        final String uri = BASE_URI + "/" + expected.getId();
        final ValidatableResponse response = post(uri, request);

        // then
        response.statusCode(OK.value())
                .body("id", equalTo(expected.getId().intValue()))
                .body("event.name", equalTo(expectedEventName))
                .body("truck.name", equalTo(expectedTruckName))
                .body("phone", equalTo(expected.getPhone()))
                .body("menus", hasSize(expectedMenuNames.length))
                .body("menus.name", contains(expectedMenuNames));
    }

    @DisplayName("접수되지 않은 주문을 취소한다.")
    @Test
    void cancel() {
        // given
        final OrderInfo order = dataSetup.saveOrderInfo(savedTruck, savedMenu);

        // when
        final OrdererInfoRequest request = new OrdererInfoRequest(order.getPhone());
        final String uri = BASE_URI + String.format("/%d/cancel", order.getId());
        final ValidatableResponse response = post(uri, request);

        // then
        response.statusCode(NO_CONTENT.value());
    }
}
