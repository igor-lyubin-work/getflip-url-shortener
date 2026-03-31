package com.getflip.urlshortener.util;

public class Base62Encoder {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = ALPHABET.length();

    public static String encode(long num) {
        if (num == 0) {
            return String.valueOf(ALPHABET.charAt(0));
        }

        StringBuilder str = new StringBuilder();
        while (num > 0) {
            str.append(ALPHABET.charAt((int) (num % BASE)));
            num /= BASE;
        }

        return str.reverse().toString();
    }
}
