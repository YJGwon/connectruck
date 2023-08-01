package com.connectruck.foodtruck.menu.domain;

import static lombok.AccessLevel.PROTECTED;

import com.connectruck.foodtruck.common.validation.Validator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
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
public class Menu {

    private static final int MAX_DESCRIPTION_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    @Include
    private Long id;

    private String name;
    private BigDecimal price;
    private boolean soldOut;
    private String description;

    private Long truckId;

    public static Menu ofNew(final String name, final BigDecimal price, final Long truckId) {
        return new Menu(null, name, price, false, null, truckId);
    }

    public void changeDescription(final String description) {
        Validator.validateMaxLength(description, MAX_DESCRIPTION_LENGTH);
        this.description = description;
    }

    public boolean isTruckId(final Long truckId) {
        return this.truckId.equals(truckId);
    }
}
