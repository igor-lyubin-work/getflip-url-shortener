package com.getflip.urlshortener.service.redis;

public interface RedisService {

    void save(String key, String value);

    String get(String key);
}
