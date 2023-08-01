package com.connectruck.foodtruck.menu.dto;

import static com.connectruck.foodtruck.common.validation.ValidationMessage.LONGER_THAN_MAX_LENGTH;

import jakarta.validation.constraints.Size;

public record MenuDescriptionRequest(
        @Size(max = 50, message = LONGER_THAN_MAX_LENGTH + ": 50자") String description
) {
}
