package com.connectruck.foodtruck.common.fixture;

import com.connectruck.foodtruck.truck.domain.Truck;
import org.springframework.data.repository.Repository;

public interface TestTruckRepository extends Repository<Truck, Long> {

    Truck save(final Truck truck);
}
