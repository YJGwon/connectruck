package com.connectruck.foodtruck.owner.domain;

import com.connectruck.foodtruck.truck.domain.Truck;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface OwnerTruckRepository extends Repository<Truck, Long> {

    Optional<Truck> findByOwnerId(final Long ownerId);
}
