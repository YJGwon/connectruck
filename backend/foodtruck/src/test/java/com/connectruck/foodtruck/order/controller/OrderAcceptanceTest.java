package com.connectruck.foodtruck.order.controller;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.CREATED;

import com.connectruck.foodtruck.common.testbase.AcceptanceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.event.service.EventService;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.order.dto.OrderMenuRequest;
import com.connectruck.foodtruck.order.dto.OrderRequest;
import com.connectruck.foodtruck.truck.domain.Participation;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;

public class OrderAcceptanceTest extends AcceptanceTestBase {

    private static final String BASE_URI = "/api/orders";


    @SpyBean
    private EventService eventService;

    @DisplayName("메뉴 주문")
    @Nested
    class order {

        @DisplayName("한 행사 참가 푸드트럭에 대해 메뉴를 주문한다.")
        @Test
        void success() {
            // given
            final Event event = 밤도깨비_야시장.create();
            dataSetup.saveEvent(event);
            doReturn(false)
                    .when(eventService)
                    .isEventClosedAt(eq(event.getId()), any(LocalDateTime.class));

            final Participation savedParticipation = dataSetup.saveParticipation(event);
            final Menu savedMenu = dataSetup.saveMenu(savedParticipation);

            final OrderRequest request = new OrderRequest(
                    savedParticipation.getId(),
                    "01000000000",
                    List.of(new OrderMenuRequest(savedMenu.getId(), 2))
            );
            // when
            final ValidatableResponse response = post(BASE_URI, request);

            // then
            response.statusCode(CREATED.value())
                    .header(LOCATION, startsWith(BASE_URI));
        }
    }
}
