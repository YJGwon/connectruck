package com.connectruck.foodtruck.notification.controller;

import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.connectruck.foodtruck.common.testbase.ControllerTestBase;
import com.connectruck.foodtruck.notification.dto.PushSubscribeRequest;
import com.connectruck.foodtruck.notification.dto.PushUnsubscribeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

public class NotificationControllerTest extends ControllerTestBase {

    private static final String BASE_URI = "/api/notification";

    @DisplayName("소유 푸드트럭의 주문 push 알림을 구독할 때 기기 등록 token이 없으면 Bad Request를 응답한다.")
    @Test
    void subscribeOrders_returnBadRequest_whenTokenIsMissing() throws Exception {
        // given & when
        final PushSubscribeRequest request = new PushSubscribeRequest(null);
        final ResultActions resultActions = performPostWithToken(BASE_URI + "/orders/my/subscription", request);

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("title").value("요청 본문이 올바르지 않습니다."))
                .andExpect(jsonPath("detail", stringContainsInOrder("필수 입력 값")));
    }

    @DisplayName("소유 푸드트럭의 주문 push 알림 구독을 해지할 때 기기 등록 token이 없으면 Bad Request를 응답한다.")
    @Test
    void unsubscribeOrders_returnBadRequest_whenTokenIsMissing() throws Exception {
        // given & when
        final PushUnsubscribeRequest request = new PushUnsubscribeRequest(null);
        final ResultActions resultActions = performDeleteWithToken(BASE_URI + "/orders/my/subscription", request);

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("title").value("요청 본문이 올바르지 않습니다."))
                .andExpect(jsonPath("detail", stringContainsInOrder("필수 입력 값")));
    }
}
