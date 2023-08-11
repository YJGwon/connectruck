package com.connectruck.foodtruck.truck.domain;

import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.Repository;

public interface TruckRepository extends Repository<Truck, Long> {

    Optional<Truck> findById(Long id);

    Slice<Truck> findByEventId(Long eventId, Pageable pageable);

    Optional<Truck> findByOwnerId(Long ownerId);
}
