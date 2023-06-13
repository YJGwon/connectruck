package com.connectruck.foodtruck.truck.dto;

import com.connectruck.foodtruck.truck.domain.Truck;
import java.util.List;
import org.springframework.data.domain.Slice;

public record TrucksResponse(
        PageResponse page,
        List<TruckResponse> trucks
) {

    public static TrucksResponse of(final Slice<Truck> truckPage) {
        final List<TruckResponse> trucks = truckPage.get()
                .map(TruckResponse::of)
                .toList();
        return new TrucksResponse(PageResponse.of(truckPage), trucks);
    }
}
