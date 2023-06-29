package com.connectruck.foodtruck.common.fixture;

import com.connectruck.foodtruck.event.domain.Participation;
import org.springframework.data.repository.Repository;

public interface TestParticipationRepository extends Repository<Participation, Long> {

    Participation save(final Participation participation);
}
