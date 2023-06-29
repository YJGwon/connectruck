package com.connectruck.foodtruck.truck.dto;

import com.connectruck.foodtruck.truck.domain.Truck;

public record TruckResponse(
        Long id,
        String name,
        String thumbnail
) {

    public static TruckResponse of(final Truck truck) {
        return new TruckResponse(
                truck.getId(),
                truck.getName(),
                truck.getThumbnail()
        );
    }
}
