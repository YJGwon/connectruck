package com.connectruck.foodtruck.menu.controller;

import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.connectruck.foodtruck.common.testbase.ControllerTestBase;
import com.connectruck.foodtruck.menu.dto.MenuDescriptionRequest;
import com.connectruck.foodtruck.menu.dto.MenuSoldOutRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

public class OwnerMenuControllerTest extends ControllerTestBase {

    private static final String BASE_URI = "/api/owner/menus";

    @DisplayName("소유 푸드트럭의 메뉴 설명을 50자를 초과하는 값으로 변경하면 BadRequest를 응답한다.")
    @Test
    void updateDescription_returnBadRequest_whenInputLengthOver50() throws Exception {
        // given
        final String descriptionOver50 = "a".repeat(51);

        // when
        final MenuDescriptionRequest request = new MenuDescriptionRequest(descriptionOver50);
        final ResultActions resultActions = performPutWithToken(BASE_URI + "/0/description", request);

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("title").value("요청 본문이 올바르지 않습니다."))
                .andExpect(jsonPath("detail", stringContainsInOrder("최대 입력 길이")));
    }

    @DisplayName("소유 푸드트럭의 품절 상태를 변경할 때 변경할 값이 없으면 BadRequest를 응답한다.")
    @Test
    void updateSoldOut_returnBadRequest_whenInputIsMissing() throws Exception {
        // given & when
        final MenuSoldOutRequest request = new MenuSoldOutRequest(null);
        final ResultActions resultActions = performPutWithToken(BASE_URI + "/0/sold-out", request);

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("title").value("요청 본문이 올바르지 않습니다."))
                .andExpect(jsonPath("detail", stringContainsInOrder("필수 입력 값")));
    }
}
