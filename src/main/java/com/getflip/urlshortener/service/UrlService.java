package com.getflip.urlshortener.service;

public interface UrlService {
    String shortenUrl(String originalUrl);

    String getOriginalUrl(String alias);
}
