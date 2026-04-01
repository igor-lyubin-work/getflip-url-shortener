package com.getflip.urlshortener.integration;

import com.getflip.urlshortener.dto.ShortenUrlRequest;
import com.getflip.urlshortener.dto.ShortenUrlResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class UrlShortenerIntegrationTest {

    @Container
    public static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testShortenAndRedirectFlow() {
        ShortenUrlRequest request = new ShortenUrlRequest("https://www.getflip.com");

        ResponseEntity<ShortenUrlResponse> createResponse = restTemplate.postForEntity(
                "/api/v1/urls", request, ShortenUrlResponse.class);

        Assertions.assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        Assertions.assertNotNull(createResponse.getBody());

        String shortUrl = createResponse.getBody().shortUrl();
        Assertions.assertNotNull(shortUrl);
        Assertions.assertTrue(shortUrl.contains("http"));

        String alias = shortUrl.substring(shortUrl.lastIndexOf("/") + 1);

        ResponseEntity<Void> redirectResponse = restTemplate.getForEntity("/" + alias, Void.class);

        Assertions.assertEquals(HttpStatus.FOUND, redirectResponse.getStatusCode());
        Assertions.assertEquals("https://www.getflip.com",
                redirectResponse.getHeaders().getLocation().toString());
    }
}
