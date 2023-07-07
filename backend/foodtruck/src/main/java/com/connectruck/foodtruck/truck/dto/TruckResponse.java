package com.connectruck.foodtruck.truck.dto;

import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.truck.domain.TruckInfo;

public record TruckResponse(
        Long id,
        String name,
        String carNumber,
        String thumbnail
) {

    public static TruckResponse of(final Truck truck) {
        final TruckInfo truckInfo = truck.getTruckInfo();
        return new TruckResponse(
                truck.getId(),
                truckInfo.getName(),
                truckInfo.getCarNumber(),
                truckInfo.getThumbnail()
        );
    }
}
