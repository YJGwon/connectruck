package com.connectruck.foodtruck.event.dto;

import com.connectruck.foodtruck.common.dto.PageResponse;
import com.connectruck.foodtruck.event.domain.Participation;
import com.connectruck.foodtruck.truck.dto.TruckResponse;
import java.util.List;
import org.springframework.data.domain.Slice;

public record ParticipationsResponse(
        PageResponse page,
        List<TruckResponse> trucks
) {

    public static ParticipationsResponse of(final Slice<Participation> participations) {
        final List<TruckResponse> trucks = participations.get()
                .map(Participation::getTruck)
                .map(TruckResponse::of)
                .toList();
        return new ParticipationsResponse(PageResponse.of(participations), trucks);
    }
}
