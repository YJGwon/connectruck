package com.connectruck.foodtruck.order.controller;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.connectruck.foodtruck.common.testbase.AcceptanceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.event.service.EventService;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.order.domain.OrderInfo;
import com.connectruck.foodtruck.order.domain.OrderLine;
import com.connectruck.foodtruck.order.dto.OrderLineRequest;
import com.connectruck.foodtruck.order.dto.OrderRequest;
import com.connectruck.foodtruck.truck.domain.Truck;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;

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

    @SpyBean
    private EventService eventService;

    @DisplayName("한 푸드트럭에 대해 메뉴를 주문한다.")
    @Test
    void order() {
        // given
        // 이벤트 진행 시간 검증 통과
        doReturn(false)
                .when(eventService)
                .isEventClosedAt(eq(event.getId()), any(LocalDateTime.class));

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

    @DisplayName("주문 상세 정보를 id로 조회한다.")
    @Test
    void findById() {
        // given
        final OrderInfo expected = dataSetup.saveOrderInfo(savedTruck, savedMenu);
        final String[] expectedMenuNames = expected.getOrderLines()
                .stream()
                .map(OrderLine::getMenuName)
                .toArray(String[]::new);

        // when
        final ValidatableResponse response = get(BASE_URI + "/" + expected.getId());

        // then
        response.statusCode(OK.value())
                .body("id", equalTo(expected.getId().intValue()))
                .body("phone", equalTo(expected.getPhone()))
                .body("menus", hasSize(expectedMenuNames.length))
                .body("menus.name", contains(expectedMenuNames));
    }
}
