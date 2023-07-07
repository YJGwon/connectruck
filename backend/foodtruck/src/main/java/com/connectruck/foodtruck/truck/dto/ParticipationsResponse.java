package com.connectruck.foodtruck.truck.dto;

import com.connectruck.foodtruck.common.dto.PageResponse;
import com.connectruck.foodtruck.truck.domain.Truck;
import java.util.List;
import org.springframework.data.domain.Slice;

public record ParticipationsResponse(
        PageResponse page,
        List<ParticipationResponse> trucks
) {

    public static ParticipationsResponse of(final Slice<Truck> trucks) {
        final List<ParticipationResponse> truckResponses = trucks.get()
                .map(ParticipationResponse::of)
                .toList();
        return new ParticipationsResponse(PageResponse.of(trucks), truckResponses);
    }
}
