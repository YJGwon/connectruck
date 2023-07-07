package com.connectruck.foodtruck.menu.service;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.menu.dto.MenuResponse;
import com.connectruck.foodtruck.menu.dto.MenusResponse;
import com.connectruck.foodtruck.truck.domain.Participation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTestBase {

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴 조회")
    @Nested
    class findById {

        @DisplayName("id로 메뉴를 조회한다.")
        @Test
        void success() {
            // given
            final Event event = 밤도깨비_야시장.create();
            dataSetup.saveEvent(event);

            final Participation savedParticipation = dataSetup.saveParticipation(event);
            final Menu expected = dataSetup.saveMenu(savedParticipation);

            // when
            final MenuResponse response = menuService.findById(expected.getId());

            // then
            assertThat(response.name()).isEqualTo(expected.getName());
        }

        @DisplayName("해당하는 메뉴가 존재하지 않으면 예외가 발생한다.")
        @Test
        void throwsException_whenMenuNotFound() {
            // given
            final Long fakeId = 0L;

            // when & then
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> menuService.findById(fakeId))
                    .withMessageContaining("존재하지 않습니다");
        }
    }

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
            final MenusResponse response = menuService.findByTruckId(savedParticipation.getId());

            // then
            assertThat(response.menus()).hasSize(2);
        }
    }
}
