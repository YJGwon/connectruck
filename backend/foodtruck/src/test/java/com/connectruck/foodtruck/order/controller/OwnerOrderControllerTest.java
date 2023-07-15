package com.connectruck.foodtruck.order.controller;

import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.connectruck.foodtruck.common.testbase.ControllerTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.web.servlet.ResultActions;

public class OwnerOrderControllerTest extends ControllerTestBase {

    private static final String BASE_URI = "/api/owner/orders";

    @DisplayName("소유 푸드트럭 주문 목록 조회")
    @Nested
    class findMyOrders {

        @DisplayName("페이지 번호 또는 사이즈가 음수일 경우 BadRequest를 응답한다.")
        @ParameterizedTest
        @ValueSource(strings = {"page", "size"})
        void returnBadRequest_whenPageIsNegative(final String paramKey) throws Exception {
            // given & when
            final String param = String.format("?%s=-1", paramKey);
            final ResultActions resultActions = performGetWithToken(BASE_URI + "/my" + param);

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("title").value("요청 파라미터값이 올바르지 않습니다."))
                    .andExpect(jsonPath("detail", stringContainsInOrder("최소값")));
        }
    }
}
