package com.getflip.urlshortener.service;

import com.getflip.urlshortener.service.redis.KeyGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KeyGeneratorTest {

    @Test
    public void generateRedisKey_consistencyTest() {
        String url = "https://www.getflip.com";

        String result1 = KeyGenerator.generateRedisKey(url);
        String result2 = KeyGenerator.generateRedisKey(url);

        assertEquals(result1, result2, "Hashes for the same URL should be identical");
    }

    @Test
    public void generateRedisKey_uniquenessTest() {
        String url1 = "https://www.getflip.com/about";
        String url2 = "https://www.getflip.com/jobs";

        String result1 = KeyGenerator.generateRedisKey(url1);
        String result2 = KeyGenerator.generateRedisKey(url2);

        assertNotEquals(result1, result2, "Different URLs should produce different keys");
    }

    @Test
    public void generateRedisKey_formatTest() {
        String url = "https://google.com/search?q=query+with+special+chars";
        String result = KeyGenerator.generateRedisKey(url);

        assertFalse(result.contains("+"), "Result should not contain '+'");
        assertFalse(result.contains("/"), "Result should not contain '/'");
        assertFalse(result.contains("="), "Result should not contain padding '='");
        assertTrue(result.length() > 0, "Result should not be empty");
    }

    @Test
    public void generateRedisKey_varyingLengths() {
        String value = "http://very-long-url-with-lots-of-parameters-and-path-segments-that-keep-going";
        String result = KeyGenerator.generateRedisKey(value);
        assertNotNull(result);
    }
}
