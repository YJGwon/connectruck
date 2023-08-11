package com.connectruck.foodtruck.event.dto;

import com.connectruck.foodtruck.event.domain.Event;
import io.swagger.v3.oas.annotations.media.Schema;

public record EventResponse(
        @Schema(description = "행사 id", example = "1")
        Long id,
        @Schema(description = "행사 이름", example = "밤도깨비 야시장 2023")
        String name,
        @Schema(description = "행사 주소", example = "서울 영등포구 여의동 여의동로 330")
        String location
) {

    public static EventResponse of(final Event event) {
        return new EventResponse(event.getId(), event.getName(), event.getLocation());
    }
}
