package com.connectruck.foodtruck.menu.controller;

import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.connectruck.foodtruck.common.testbase.ControllerTestBase;
import com.connectruck.foodtruck.menu.dto.MenuDescriptionRequest;
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
}
