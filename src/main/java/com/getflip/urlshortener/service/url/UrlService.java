package com.getflip.urlshortener.service.url;

public interface UrlService {
    String shortenUrl(String originalUrl);

    String getOriginalUrl(String alias);
}
