package com.connectruck.foodtruck.user.domain;

import static lombok.AccessLevel.PROTECTED;

import com.connectruck.foodtruck.common.validation.Validator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Include
    private Long id;

    private String username;
    private EncodedPassword password;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;

    public Account(final Long id,
                   final String username,
                   final EncodedPassword password,
                   final String phone,
                   final Role role) {
        Validator.validatePhone(phone);
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

    public static Account ofNew(final String username, final String password, final String phone, final Role role) {
        return new Account(null, username, EncodedPassword.fromPlainText(password), phone, role);
    }

    public boolean isPassword(final String password) {
        return this.password.isSamePassword(password);
    }
}
