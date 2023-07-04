package com.connectruck.foodtruck.menu.domain;

import static com.connectruck.foodtruck.common.fixture.data.EventFixture.밤도깨비_야시장;
import static org.assertj.core.api.Assertions.assertThat;

import com.connectruck.foodtruck.common.testbase.RepositoryTestBase;
import com.connectruck.foodtruck.event.domain.Event;
import com.connectruck.foodtruck.truck.domain.Participation;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuRepositoryTest extends RepositoryTestBase {

    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("참가 푸드트럭의 메뉴 목록을 조회한다.")
    @Test
    void findByParticipationId() {
        // given
        final Event event = 밤도깨비_야시장.create();
        dataSetup.saveEvent(event);

        final Participation savedParticipation = dataSetup.saveParticipation(event);
        dataSetup.saveMenu(savedParticipation);
        dataSetup.saveMenu(savedParticipation);

        final Participation otherParticipation = dataSetup.saveParticipation(event);
        dataSetup.saveMenu(otherParticipation);

        // when
        final List<Menu> found = menuRepository.findByParticipationId(savedParticipation.getId());

        // when
        assertThat(found).hasSize(2);
    }
}
