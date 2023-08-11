package com.connectruck.foodtruck.menu.dto;

import static com.connectruck.foodtruck.common.validation.ValidationMessage.LONGER_THAN_MAX_LENGTH;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record MenuDescriptionRequest(
        @Schema(description = "메뉴 상세 설명", example = "맛있는 소세지 핫도그")
        @Size(max = 50, message = LONGER_THAN_MAX_LENGTH + ": 50자")
        String description
) {
}
