package com.connectruck.foodtruck.owner.service;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.owner.dto.OwnerTruckResponse;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.truck.domain.TruckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OwnerService {

    private final TruckRepository truckRepository;

    public OwnerTruckResponse findOwningTruck(final Long ownerId) {
        final Truck found = truckRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> NotFoundException.of("푸드트럭", "ownerId", ownerId));

        return OwnerTruckResponse.of(found);
    }
}
