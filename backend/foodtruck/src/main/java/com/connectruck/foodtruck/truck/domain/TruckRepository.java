package com.connectruck.foodtruck.truck.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.Repository;

public interface TruckRepository extends Repository<Truck, Long> {

    Slice<Truck> findAllBy(final Pageable pageable);
}
