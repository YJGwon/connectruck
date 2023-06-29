package com.connectruck.foodtruck.participation.domain;


import static lombok.AccessLevel.PROTECTED;

import com.connectruck.foodtruck.truck.domain.Truck;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participation_id")
    private Long id;

    @Include
    private Long eventId;

    @ManyToOne
    @JoinColumn(name = "truck_id")
    @Include
    private Truck truck;

    public static Participation ofNew(final Long eventId, final Truck truck) {
        return new Participation(null, eventId, truck);
    }
}
