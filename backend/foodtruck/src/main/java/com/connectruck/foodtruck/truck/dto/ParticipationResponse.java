package com.connectruck.foodtruck.truck.dto;

import com.connectruck.foodtruck.truck.domain.Participation;
import com.connectruck.foodtruck.truck.domain.Truck;

public record ParticipationResponse(
        Long id,
        String name,
        String carNumber,
        String thumbnail
) {

    public static ParticipationResponse of(final Participation participation) {
        final Truck truck = participation.getTruck();
        return new ParticipationResponse(
                participation.getId(),
                truck.getName(),
                truck.getCarNumber(),
                truck.getThumbnail()
        );
    }
}
