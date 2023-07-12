package com.connectruck.foodtruck.order.controller;

import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.connectruck.foodtruck.common.testbase.ControllerTestBase;
import com.connectruck.foodtruck.order.dto.OrderLineRequest;
import com.connectruck.foodtruck.order.dto.OrderRequest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.web.servlet.ResultActions;

class OrderControllerTest extends ControllerTestBase {

    private static final String BASE_URI = "/api/orders";

    @DisplayName("메뉴 주문")
    @Nested
    class create {

        private static final String PHONE = "01000000000";
        private static final List<OrderLineRequest> MENUS = List.of(new OrderLineRequest(1L, 2));

        @DisplayName("푸드트럭 id가 없을 경우 Bad Request를 응답한다.")
        @Test
        void returnBadRequest_whenTruckIdIsNull() throws Exception {
            // given
            final OrderRequest request = new OrderRequest(null, PHONE, MENUS);

            // when
            final ResultActions resultActions = performPost(BASE_URI, request);

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("title").value("요청 본문이 올바르지 않습니다."))
                    .andExpect(jsonPath("detail", stringContainsInOrder("필수", "푸드트럭 id")));
        }

        @DisplayName("휴대폰 번호 형식이 잘못되었을 경우 Bad Request를 응답한다.")
        @ParameterizedTest
        @ValueSource(strings = {"11012341234", "01212341234", "010121234", "010a1231234", "010-1234-1234"})
        void returnBadRequest_whenPhoneInvalid(final String invalidPhone) throws Exception {
            // given
            final OrderRequest request = new OrderRequest(null, invalidPhone, MENUS);

            // when
            final ResultActions resultActions = performPost(BASE_URI, request);

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("title").value("요청 본문이 올바르지 않습니다."))
                    .andExpect(jsonPath("detail", stringContainsInOrder("형식", "휴대폰 번호")));
        }

        @DisplayName("주문 메뉴 목록이 비어있을 경우 Bad Request를 응답한다.")
        @ParameterizedTest
        @NullAndEmptySource
        void returnBadRequest_whenMenuIsBlank(final List<OrderLineRequest> blankMenus) throws Exception {
            // given
            final OrderRequest request = new OrderRequest(1L, PHONE, blankMenus);

            // when
            final ResultActions resultActions = performPost(BASE_URI, request);

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("title").value("요청 본문이 올바르지 않습니다."))
                    .andExpect(jsonPath("detail", stringContainsInOrder("필수", "주문 메뉴")));
        }

        @DisplayName("주문 메뉴의 메뉴 id가 없을 경우 Bad Request를 응답한다.")
        @Test
        void returnBadRequest_whenMenuIdIsNull() throws Exception {
            // given
            final OrderRequest request = new OrderRequest(1L, PHONE, List.of(new OrderLineRequest(null, 2)));

            // when
            final ResultActions resultActions = performPost(BASE_URI, request);

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("title").value("요청 본문이 올바르지 않습니다."))
                    .andExpect(jsonPath("detail", stringContainsInOrder("필수", "메뉴 id")));
        }

        @DisplayName("주문 메뉴의 수량이 0 이하일 경우 Bad Request를 응답한다.")
        @ParameterizedTest
        @ValueSource(ints = {0, -1})
        void returnBadRequest_whenQuantityIsNotPositive(final int quantity) throws Exception {
            // given
            final OrderRequest request = new OrderRequest(1L, PHONE, List.of(new OrderLineRequest(1L, quantity)));

            // when
            final ResultActions resultActions = performPost(BASE_URI, request);

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("title").value("요청 본문이 올바르지 않습니다."))
                    .andExpect(jsonPath("detail", stringContainsInOrder("최소값", "수량")));
        }
    }
}
