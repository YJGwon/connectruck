package com.connectruck.foodtruck.owner.service;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.owner.domain.OwnerTruckRepository;
import com.connectruck.foodtruck.owner.dto.OwnerTruckResponse;
import com.connectruck.foodtruck.truck.domain.Truck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerTruckRepository ownerTruckRepository;

    public OwnerTruckResponse findOwningTruck(final Long ownerId) {
        final Truck found = ownerTruckRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> NotFoundException.of("푸드트럭", "ownerId", ownerId));

        return OwnerTruckResponse.of(found);
    }
}
