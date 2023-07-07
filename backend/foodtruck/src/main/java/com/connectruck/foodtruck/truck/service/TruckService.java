package com.connectruck.foodtruck.truck.service;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.truck.domain.TruckRepository;
import com.connectruck.foodtruck.truck.dto.ParticipationResponse;
import com.connectruck.foodtruck.truck.dto.ParticipationsResponse;
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

    public ParticipationsResponse findByEvent(final Long eventId, final int page, final int size) {
        final Slice<Truck> found = truckRepository.findByEventId(eventId,
                PageRequest.of(page, size));
        return ParticipationsResponse.of(found);
    }

    public ParticipationResponse findByParticipationId(final Long truckId) {
        final Truck found = truckRepository.findById(truckId)
                .orElseThrow(() -> createParticipationNotFoundException(truckId));

        return ParticipationResponse.of(found);
    }

    public Long findEventIdByParticipationId(final Long truckId) {
        final Truck found = truckRepository.findById(truckId)
                .orElseThrow(() -> createParticipationNotFoundException(truckId));

        return found.getEventId();
    }

    private NotFoundException createParticipationNotFoundException(final Long truckId) {
        return NotFoundException.of("푸드트럭", truckId);
    }
}
