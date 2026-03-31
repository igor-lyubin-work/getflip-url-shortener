package com.getflip.urlshortener.controller;

import com.getflip.urlshortener.dto.ShortenUrlRequest;
import com.getflip.urlshortener.dto.ShortenUrlResponse;
import com.getflip.urlshortener.service.UrlServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
public class UrlController {

    private final UrlServiceImpl urlService;

    @PostMapping
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest request,
                                                         HttpServletRequest httpRequest) {
        String alias = urlService.shortenUrl(request.url());

        String shortUrl = ServletUriComponentsBuilder.fromContextPath(httpRequest)
                .path("/" + alias)
                .build()
                .toUriString();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ShortenUrlResponse(shortUrl, request.url()));
    }

    @GetMapping("")
    public ResponseEntity<Void> redirectUrl(@RequestParam String alias) {
        String originalUrl = urlService.getOriginalUrl(alias);

        if (originalUrl == null) {
            log.error("Key not found in Redis: {}", alias);
            return ResponseEntity.notFound()
                    .build();
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}
