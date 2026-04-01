package com.getflip.urlshortener.controller;

import com.getflip.urlshortener.dto.ShortenUrlRequest;
import com.getflip.urlshortener.dto.ShortenUrlResponse;
import com.getflip.urlshortener.service.url.UrlServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping(UrlController.BASE_PATH)
@RequiredArgsConstructor
public class UrlController {
    public static final String BASE_PATH = "/api/v1/urls";

    private final UrlServiceImpl urlService;


    @PostMapping
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest request,
                                                         HttpServletRequest httpRequest) {
        String alias = urlService.shortenUrl(request.url());

        String shortUrl = ServletUriComponentsBuilder.fromContextPath(httpRequest)
                .path(BASE_PATH + "/" + alias)
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
