package com.connectruck.foodtruck.truck.dto;

import com.connectruck.foodtruck.truck.domain.Truck;
import io.swagger.v3.oas.annotations.media.Schema;

public record TruckResponse(
        @Schema(description = "푸드트럭 id", example = "1")
        Long id,
        @Schema(description = "푸드트럭 이름", example = "핫도그사세요")
        String name,
        @Schema(description = "차랑번호", example = "23가1234")
        String carNumber,
        @Schema(description = "차량 사진 url", example = "sample.png")
        String thumbnail
) {

    public static TruckResponse of(final Truck truck) {
        return new TruckResponse(
                truck.getId(),
                truck.getName(),
                truck.getCarNumber(),
                truck.getThumbnail()
        );
    }
}
