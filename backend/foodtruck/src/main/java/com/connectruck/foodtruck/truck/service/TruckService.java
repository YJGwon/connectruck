package com.connectruck.foodtruck.truck.service;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.truck.domain.TruckRepository;
import com.connectruck.foodtruck.truck.dto.TruckResponse;
import com.connectruck.foodtruck.truck.dto.TrucksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TruckService {

    private final TruckRepository truckRepository;

    public TruckResponse findById(final Long id) {
        final Truck found = getOneById(id);
        return TruckResponse.of(found);
    }

    public TrucksResponse findByEvent(final Long eventId, final int page, final int size) {
        final Slice<Truck> found = truckRepository.findByEventId(eventId,
                PageRequest.of(page, size));
        return TrucksResponse.of(found);
    }

    public TruckResponse findByOwnerId(final Long ownerId) {
        final Truck found = truckRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> NotFoundException.of("푸드트럭", "ownerId", ownerId));
        return TruckResponse.of(found);
    }

    public Long findEventIdById(final Long id) {
        final Truck found = getOneById(id);
        return found.getEventId();
    }

    public Long findOwnerIdById(Long id) {
        final Truck found = getOneById(id);
        return found.getOwnerId();
    }

    private Truck getOneById(final Long id) {
        return truckRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("푸드트럭", "truckId", id));
    }
}
