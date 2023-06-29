package com.connectruck.foodtruck.participation.service;

import com.connectruck.foodtruck.participation.domain.Participation;
import com.connectruck.foodtruck.participation.domain.ParticipationRepository;
import com.connectruck.foodtruck.participation.dto.ParticipationsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipationService {

    private final ParticipationRepository participationRepository;

    public ParticipationsResponse findByEvent(final Long eventId, final int page, final int size) {
        final Slice<Participation> found = participationRepository.findByEventId(eventId,
                PageRequest.of(page, size));
        return ParticipationsResponse.of(found);
    }
}
