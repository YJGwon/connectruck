package com.connectruck.foodtruck.truck.service;

import com.connectruck.foodtruck.truck.domain.Participation;
import com.connectruck.foodtruck.truck.domain.ParticipationRepository;
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

    private final ParticipationRepository participationRepository;

    public TrucksResponse findByEvent(final Long eventId, final int page, final int size) {
        final Slice<Participation> found = participationRepository.findByEventId(eventId,
                PageRequest.of(page, size));
        return TrucksResponse.of(found);
    }
}
