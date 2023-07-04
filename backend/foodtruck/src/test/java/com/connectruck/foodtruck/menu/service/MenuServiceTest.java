package com.connectruck.foodtruck.menu.service;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThat;

import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.menu.dto.MenusResponse;
import com.connectruck.foodtruck.truck.domain.Participation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTestBase {

    @Autowired
    private MenuService menuService;

    @DisplayName("참가 푸드트럭 메뉴 목록 조회")
    @Nested
    class findByParticipationId {

        @DisplayName("참가 푸드트럭의 id로 메뉴 목록을 조회한다.")
        @Test
        void success() {
            // given
            final Event event = 밤도깨비_야시장.create();
            dataSetup.saveEvent(event);

            final Participation savedParticipation = dataSetup.saveParticipation(event);
            dataSetup.saveMenu(savedParticipation);
            dataSetup.saveMenu(savedParticipation);

            // when
            final MenusResponse response = menuService.findByParticipationId(savedParticipation.getId());

            // then
            assertThat(response.menus()).hasSize(2);
        }
    }
}
