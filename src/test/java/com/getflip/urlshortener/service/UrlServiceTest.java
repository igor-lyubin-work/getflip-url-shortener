package com.getflip.urlshortener.service;

import com.getflip.urlshortener.service.redis.RedisService;
import com.getflip.urlshortener.service.url.UrlServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    private RedisService redisService;

    @InjectMocks
    private UrlServiceImpl urlService;

    private final String TEST_URL = "https://www.getflip.com.com";

    @Test
    public void shortenUrl_shouldSaveToRedisAndReturnAlias() {
        String result = urlService.shortenUrl(TEST_URL);

        assertNotNull(result);
        verify(redisService, times(1)).save(anyString(), eq(TEST_URL));
    }

    @Test
    public void getOriginalUrl_shouldReturnUrlFromRedis_WhenAliasExists() {
        String testAlias = "abc12345";
        when(redisService.get(testAlias)).thenReturn(TEST_URL);

        String result = urlService.getOriginalUrl(testAlias);

        assertEquals(TEST_URL, result);
        verify(redisService, times(1)).get(testAlias);
    }

    @Test
    public void getOriginalUrl_shouldReturnNull_WhenAliasDoesNotExist() {
        when(redisService.get("invalid")).thenReturn(null);

        String result = urlService.getOriginalUrl("invalid");

        assertNull(result);
    }
}
