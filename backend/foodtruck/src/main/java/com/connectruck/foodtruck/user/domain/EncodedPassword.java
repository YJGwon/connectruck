package com.connectruck.foodtruck.user.domain;

import static lombok.AccessLevel.PROTECTED;

import com.connectruck.foodtruck.common.constant.FormatText;
import com.connectruck.foodtruck.common.exception.InvalidFormatException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Getter
public final class EncodedPassword {

    private static final Pattern PATTERN = Pattern.compile(FormatText.PASSWORD);

    @Column(name = "password")
    private String value;

    public static EncodedPassword fromPlainText(final String plainText) {
        validateFormat(plainText);
        return new EncodedPassword(encrypt(plainText));
    }

    private static void validateFormat(final String plainText) {
        final Matcher matcher = PATTERN.matcher(plainText);
        if (!matcher.matches()) {
            throw InvalidFormatException.withFormatDescription("비밀번호", FormatText.PASSWORD_DESCRIPTION, plainText);
        }
    }

    private static String encrypt(final String plainText) {
        final MessageDigest messageDigest = getMessageDigestInstance();
        final byte[] hash = messageDigest.digest(plainText.getBytes(StandardCharsets.UTF_8));

        return bytesToHexString(hash);
    }

    public boolean isSamePassword(final String plainText) {
        return value.equals(encrypt(plainText));
    }

    private static String bytesToHexString(final byte[] bytes) {
        final StringBuilder builder = new StringBuilder();
        for (final byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    private static MessageDigest getMessageDigestInstance() {
        try {
            return MessageDigest.getInstance("SHA3-256");
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
