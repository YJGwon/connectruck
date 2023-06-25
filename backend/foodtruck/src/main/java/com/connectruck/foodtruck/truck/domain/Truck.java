package com.connectruck.foodtruck.truck.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "truck_id")
    @Include
    private Long id;

    private String name;
    private String thumbnail;
    private String location;
    private LocalTime openHour;
    private LocalTime closeHour;

    public static Truck ofNew(final String name, final String thumbnail, final String location,
                              final LocalTime openHour, final LocalTime closeHour) {
        return new Truck(null, name, thumbnail, location, openHour, closeHour);
    }

    public static Truck ofNewWithNoThumbnail(final String name, final String location,
                                             final LocalTime openHour, final LocalTime closeHour) {
        return new Truck(null, name, null, location, openHour, closeHour);
    }
}
