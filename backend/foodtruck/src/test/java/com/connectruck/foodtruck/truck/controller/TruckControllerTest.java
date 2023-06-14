package com.connectruck.foodtruck.truck.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.connectruck.foodtruck.common.testbase.ControllerTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.web.servlet.ResultActions;

class TruckControllerTest extends ControllerTestBase {

    @DisplayName("푸드트럭 전체 목록 조회")
    @Nested
    class findAll {

        @DisplayName("페이지 번호 또는 사이즈가 음수일 경우 BadRequest를 응답한다.")
        @ParameterizedTest
        @ValueSource(strings = {"page", "size"})
        void returnBadRequest_whenPageIsNegative(final String paramKey) throws Exception {
            // given & when
            final ResultActions resultActions = performGet(String.format("/api/trucks?%s=-1", paramKey));

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("title").value("요청 파라미터값이 올바르지 않습니다."));
        }
    }
}
