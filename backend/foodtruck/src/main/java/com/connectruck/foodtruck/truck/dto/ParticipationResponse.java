package com.connectruck.foodtruck.truck.dto;

import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.truck.domain.TruckInfo;

public record ParticipationResponse(
        Long id,
        String name,
        String carNumber,
        String thumbnail
) {

    public static ParticipationResponse of(final Truck truck) {
        final TruckInfo truckInfo = truck.getTruckInfo();
        return new ParticipationResponse(
                truck.getId(),
                truckInfo.getName(),
                truckInfo.getCarNumber(),
                truckInfo.getThumbnail()
        );
    }
}
