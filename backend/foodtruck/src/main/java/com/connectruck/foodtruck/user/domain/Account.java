package com.connectruck.foodtruck.user.domain;

import static lombok.AccessLevel.PROTECTED;

import com.connectruck.foodtruck.common.constant.FormatText;
import com.connectruck.foodtruck.common.exception.InvalidFormatException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {

    private static final Pattern PATTERN = Pattern.compile(FormatText.PHONE);

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
        validatePhone(phone);
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

    public static Account ofNew(final String username, final String password, final String phone, final Role role) {
        return new Account(null, username, EncodedPassword.fromPlainText(password), phone, role);
    }

    private static void validatePhone(final String phone) {
        final Matcher matcher = PATTERN.matcher(phone);
        if (!matcher.matches()) {
            throw InvalidFormatException.withInputValue("휴대폰 번호", phone);
        }
    }
}
