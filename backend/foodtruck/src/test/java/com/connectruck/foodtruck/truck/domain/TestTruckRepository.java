package com.connectruck.foodtruck.truck.domain;

import org.springframework.data.repository.Repository;

public interface TestTruckRepository extends Repository<Truck, Long> {

    Truck save(final Truck truck);
}
