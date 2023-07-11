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

    public TrucksResponse findByEvent(final Long eventId, final int page, final int size) {
        final Slice<Truck> found = truckRepository.findByEventId(eventId,
                PageRequest.of(page, size));
        return TrucksResponse.of(found);
    }

    public TruckResponse findById(final Long id) {
        final Truck found = truckRepository.findById(id)
                .orElseThrow(() -> createTruckNotFoundException("truckId", id));

        return TruckResponse.of(found);
    }


    public TruckResponse findByOwnerId(final Long ownerId) {
        final Truck found = truckRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> createTruckNotFoundException("ownerId", ownerId));

        return TruckResponse.of(found);
    }

    public Long findEventIdById(final Long id) {
        final Truck found = truckRepository.findById(id)
                .orElseThrow(() -> createTruckNotFoundException("truckId", id));

        return found.getEventId();
    }

    private NotFoundException createTruckNotFoundException(final String paramName, final Long truckId) {
        return NotFoundException.of("푸드트럭", paramName, truckId);
    }
}
