package com.connectruck.foodtruck.menu.dto;

import com.connectruck.foodtruck.menu.domain.Menu;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record MenuResponse(
        @Schema(description = "메뉴 id", example = "1")
        Long id,
        @Schema(description = "메뉴 이름", example = "핫도그")
        String name,
        @Schema(description = "메뉴 가격", example = "4500")
        BigDecimal price,
        @Schema(description = "품절 여부", example = "false")
        boolean soldOut,
        @Schema(description = "상세 설명", example = "맛있는 소세지 핫도그")
        String description,
        @Schema(description = "푸드트럭 id", example = "1")
        @JsonIgnore Long truckId
) {

    public static MenuResponse of(final Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.isSoldOut(),
                menu.getDescription(),
                menu.getTruckId()
        );
    }
}
