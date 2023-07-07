package com.connectruck.foodtruck.truck.domain;

import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.Repository;

public interface TruckRepository extends Repository<Truck, Long> {

    Optional<Truck> findById(final Long id);

    Slice<Truck> findByEventId(final Long eventId, final Pageable pageable);
}
