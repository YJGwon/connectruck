package com.connectruck.foodtruck.user.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Getter
public final class EncodedPassword {

    @Column(name = "password")
    private String value;

    public static EncodedPassword fromPlainText(final String plainText) {
        return new EncodedPassword(encrypt(plainText));
    }

    private static String encrypt(final String text) {
        final MessageDigest messageDigest = getMessageDigestInstance();
        final byte[] hash = messageDigest.digest(text.getBytes(StandardCharsets.UTF_8));

        return bytesToHexString(hash);
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

    public boolean isSamePassword(final String plainText) {
        return value.equals(encrypt(plainText));
    }
}
