package com.getflip.urlshortener.check;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisStartupCheck implements SmartInitializingSingleton {

    private final RedisConnectionFactory redisConnectionFactory;

    public RedisStartupCheck(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    public void afterSingletonsInstantiated() {
        try {
            redisConnectionFactory.getConnection().ping();
        } catch (Exception e) {
            throw new IllegalStateException("Redis is unavailable at startup! Failing fast.", e);
        }
    }
}
