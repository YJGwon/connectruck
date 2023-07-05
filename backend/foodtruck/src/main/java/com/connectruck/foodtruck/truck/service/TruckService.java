package com.connectruck.foodtruck.truck.service;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.truck.domain.Participation;
import com.connectruck.foodtruck.truck.domain.ParticipationRepository;
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

    private final ParticipationRepository participationRepository;

    public ParticipationsResponse findByEvent(final Long eventId, final int page, final int size) {
        final Slice<Participation> found = participationRepository.findByEventId(eventId,
                PageRequest.of(page, size));
        return ParticipationsResponse.of(found);
    }

    public ParticipationResponse findByParticipationId(final Long participationId) {
        final Participation found = participationRepository.findById(participationId)
                .orElseThrow(() -> createParticipationNotFoundException(participationId));

        return ParticipationResponse.of(found);
    }

    public Long findEventIdByParticipationId(final Long participationId) {
        final Participation found = participationRepository.findById(participationId)
                .orElseThrow(() -> createParticipationNotFoundException(participationId));

        return found.getEventId();
    }

    private NotFoundException createParticipationNotFoundException(final Long participationId) {
        return NotFoundException.of("푸드트럭", participationId);
    }
}
