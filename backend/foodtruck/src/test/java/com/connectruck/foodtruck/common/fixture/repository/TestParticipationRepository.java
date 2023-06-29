package com.connectruck.foodtruck.common.fixture.repository;

import com.connectruck.foodtruck.participation.domain.Participation;
import org.springframework.data.repository.Repository;

public interface TestParticipationRepository extends Repository<Participation, Long> {

    Participation save(final Participation participation);
}
