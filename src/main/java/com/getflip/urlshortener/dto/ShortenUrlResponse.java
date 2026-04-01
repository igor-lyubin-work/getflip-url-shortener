package com.getflip.urlshortener.dto;

public record ShortenUrlResponse(
        String shortUrl,
        String longUrl
) {
}