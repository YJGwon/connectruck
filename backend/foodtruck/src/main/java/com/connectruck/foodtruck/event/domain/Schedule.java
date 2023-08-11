package com.connectruck.foodtruck.event.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    @Include
    private Long id;

    private LocalDate eventDate;
    private LocalTime openHour;
    private LocalTime closeHour;
    private Long eventId;

    public static Schedule ofNew(final LocalDate eventDate,
                                 final LocalTime openHour,
                                 final LocalTime closeHour,
                                 final Long eventId) {
        return new Schedule(null, eventDate, openHour, closeHour, eventId);
    }

    public boolean isClosedAt(final LocalTime time) {
        return openHour.isAfter(time) || closeHour.isBefore(time);
    }
}
