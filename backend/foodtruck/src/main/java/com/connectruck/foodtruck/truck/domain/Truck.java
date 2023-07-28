package com.connectruck.foodtruck.truck.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
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

    @Transient
    public static final Truck NULL = Truck.ofNew(null, "존재하지 않는 푸드트럭", null, null, null);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "truck_id")
    @Include
    private Long id;

    private Long eventId;
    private String name;
    private String carNumber;
    private String thumbnail;
    private Long ownerId;

    public static Truck ofNew(final Long eventId,
                              final String name,
                              final String carNumber,
                              final String thumbnail,
                              final Long ownerId) {
        return new Truck(null, eventId, name, carNumber, thumbnail, ownerId);
    }
}
