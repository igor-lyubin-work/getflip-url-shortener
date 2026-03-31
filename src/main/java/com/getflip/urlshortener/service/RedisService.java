package com.getflip.urlshortener.service;

public interface RedisService {

    long generateKey();

    void save(String key, String value);

    String get(String key);
}
