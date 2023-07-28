package com.connectruck.foodtruck.order.dto;

import com.connectruck.foodtruck.truck.domain.Truck;

public record OrderedTruckResponse(
        Long id,
        String name,
        String carNumber,
        String thumbnail
) {

    public static OrderedTruckResponse of(final Truck truck) {
        return new OrderedTruckResponse(
                truck.getId(),
                truck.getName(),
                truck.getCarNumber(),
                truck.getThumbnail()
        );
    }
}
