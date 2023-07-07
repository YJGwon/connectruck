package com.connectruck.foodtruck.truck.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Truck {

    private String name;
    private String carNumber;
    private String thumbnail;

    public static Truck ofNew(final String name, final String carNumber, final String thumbnail) {
        return new Truck(name, carNumber, thumbnail);
    }

    public static Truck ofNewWithNoThumbnail(final String name, final String carNumber) {
        return new Truck(name, carNumber, null);
    }
}
