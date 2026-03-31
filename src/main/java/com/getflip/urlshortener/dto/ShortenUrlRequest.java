package com.getflip.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

public record ShortenUrlRequest(
        @NotBlank(message = "URL cannot be blank")
        @URL(message = "Invalid URL format")
        String url
) {}
