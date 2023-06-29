package com.connectruck.foodtruck.event.service;

import com.connectruck.foodtruck.event.domain.Participation;
import com.connectruck.foodtruck.event.domain.ParticipationRepository;
import com.connectruck.foodtruck.event.dto.ParticipationsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {

    private final ParticipationRepository participationRepository;

    public ParticipationsResponse findTrucks(final Long eventId, final int page, final int size) {
        final Slice<Participation> found = participationRepository.findByEventId(eventId,
                PageRequest.of(page, size));
        return ParticipationsResponse.of(found);
    }
}
