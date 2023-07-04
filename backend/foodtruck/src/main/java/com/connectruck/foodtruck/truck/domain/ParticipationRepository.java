package com.connectruck.foodtruck.truck.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.Repository;

public interface ParticipationRepository extends Repository<Participation, Long> {

    Slice<Participation> findByEventId(final Long eventId, final Pageable pageable);
}
