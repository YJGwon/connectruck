package com.connectruck.foodtruck.truck.dto;

import com.connectruck.foodtruck.common.constant.FormatText;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record TruckResponse(
        Long id,
        String name,
        String thumbnail,
        String location,
        @JsonFormat(pattern = FormatText.TIME) LocalTime openHour,
        @JsonFormat(pattern = FormatText.TIME) LocalTime closeHour
) {

    public static TruckResponse of(final Truck truck) {
        return new TruckResponse(
                truck.getId(),
                truck.getName(),
                truck.getThumbnail(),
                truck.getLocation(),
                truck.getOpenHour(),
                truck.getCloseHour()
        );
    }
}
