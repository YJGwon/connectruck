package com.connectruck.foodtruck.truck.dto;

import com.connectruck.foodtruck.common.dto.PageResponse;
import com.connectruck.foodtruck.truck.domain.Participation;
import java.util.List;
import org.springframework.data.domain.Slice;

public record TrucksResponse(
        PageResponse page,
        List<TruckResponse> trucks
) {

    public static TrucksResponse of(final Slice<Participation> participations) {
        final List<TruckResponse> trucks = participations.get()
                .map(Participation::getTruck)
                .map(TruckResponse::of)
                .toList();
        return new TrucksResponse(PageResponse.of(participations), trucks);
    }
}
