package com.getflip.urlshortener.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "url:counter";
    private static final String URL_ALIAS = "url:alias:";

    @Setter
    @Value("${app.redis.ttl-days:7}")
    private long ttlDays;

    @Override
    public long generateKey() {
        Long id = redisTemplate.opsForValue().increment(KEY_PREFIX);
        if (id == null) {
            log.error("Redis INCR operation returned null during ID generation");
            throw new RuntimeException("Failed to generate unique ID from Redis");
        }

        return id;
    }

    @Override
    public void save(String key, String value) {
        redisTemplate.opsForValue().set(URL_ALIAS + key, value, Duration.ofDays(ttlDays));
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(URL_ALIAS + key);
    }
}
