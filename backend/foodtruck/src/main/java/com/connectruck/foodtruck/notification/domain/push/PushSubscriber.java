package com.connectruck.foodtruck.notification.domain.push;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class PushSubscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "push_subscriber_id")
    @Include
    private Long id;

    private String token;
    private boolean active;
    private Long truckId;

    public static PushSubscriber ofNew(final String token, final Long truckId) {
        return new PushSubscriber(null, token, true, truckId);
    }
}
