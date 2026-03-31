package com.getflip.urlshortener.util;

import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@UtilityClass
public class Base62Encoder {

    public String generateRedisKey(String url) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(url.getBytes(StandardCharsets.UTF_8));

            byte[] shortHash = new byte[8];
            System.arraycopy(hash, 0, shortHash, 0, 8);
            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(shortHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not support", e);
        }
    }
}
