package com.connectruck.foodtruck.truck.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TruckInfo {

    private String name;
    private String carNumber;
    private String thumbnail;

    public static TruckInfo ofNew(final String name, final String carNumber, final String thumbnail) {
        return new TruckInfo(name, carNumber, thumbnail);
    }

    public static TruckInfo ofNewWithNoThumbnail(final String name, final String carNumber) {
        return new TruckInfo(name, carNumber, null);
    }
}
