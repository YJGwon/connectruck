package com.connectruck.foodtruck.event.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.connectruck.foodtruck.common.fixture.DataSetup;
import com.connectruck.foodtruck.common.testbase.RepositoryTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import(DataSetup.class)
@Sql("classpath:truncate.sql")
class ParticipationRepositoryTest extends RepositoryTestBase {

    @Autowired
    private ParticipationRepository participationRepository;

    @DisplayName("행사 참가 푸드트럭에 대해 slice 단위로 조회한다.")
    @Test
    void findAllBy_perSlice() {
        // given
        final Event event = Event.ofNew("여의도 밤도깨비 야시장", "서울 영등포구 여의동 여의동로 330");
        dataSetup.saveEvent(event);
        dataSetup.saveParticipation(event);
        dataSetup.saveParticipation(event);
        dataSetup.saveParticipation(event);

        // when
        final int page = 0;
        final int size = 2;
        final Slice<Participation> found = participationRepository.findByEventId(event.getId(),
                PageRequest.of(page, size));

        // then
        assertThat(found.getContent()).hasSize(size);
    }
}
