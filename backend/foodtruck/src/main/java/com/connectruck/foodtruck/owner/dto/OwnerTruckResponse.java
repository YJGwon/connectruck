package com.connectruck.foodtruck.owner.dto;

import com.connectruck.foodtruck.truck.domain.Truck;

public record OwnerTruckResponse(
        Long id,
        String name,
        String carNumber,
        String thumbnail
) {

    public static OwnerTruckResponse of(final Truck truck) {
        return new OwnerTruckResponse(
                truck.getId(),
                truck.getName(),
                truck.getCarNumber(),
                truck.getThumbnail()
        );
    }
}
