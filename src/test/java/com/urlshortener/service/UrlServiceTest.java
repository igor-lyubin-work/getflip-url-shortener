package com.urlshortener.service;

import com.getflip.urlshortener.service.RedisService;
import com.getflip.urlshortener.service.UrlServiceImpl;
import com.getflip.urlshortener.util.Base62Encoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UrlServiceTest {

    @Mock
    private RedisService redisService;

    @InjectMocks
    private UrlServiceImpl urlService;

    private final String ORIGINAL_URL = "https://www.getflip.com";
    private final long GENERATED_ID = 12345L;
    private final String ENCODED_ALIAS = "abc123";

    @Test
    void shortenUrl_shouldGenerateAliasAndSave() {
        when(redisService.generateKey()).thenReturn(GENERATED_ID);

        try (MockedStatic<Base62Encoder> mockedEncoder = mockStatic(Base62Encoder.class)) {
            mockedEncoder.when(() -> Base62Encoder.encode(GENERATED_ID))
                    .thenReturn(ENCODED_ALIAS);

            String result = urlService.shortenUrl(ORIGINAL_URL);

            assertEquals(ENCODED_ALIAS, result);

            verify(redisService).generateKey();
            verify(redisService).save(ENCODED_ALIAS, ORIGINAL_URL);
        }
    }

    @Test
    void shortenUrl_shouldHandleDifferentIds() {
        long anotherId = 999L;
        String anotherAlias = "xyz789";

        when(redisService.generateKey()).thenReturn(anotherId);

        try (MockedStatic<Base62Encoder> mockedEncoder = mockStatic(Base62Encoder.class)) {
            mockedEncoder.when(() -> Base62Encoder.encode(anotherId))
                    .thenReturn(anotherAlias);

            String result = urlService.shortenUrl(ORIGINAL_URL);

            assertEquals(anotherAlias, result);
            verify(redisService).save(anotherAlias, ORIGINAL_URL);
        }
    }

    @Test
    void getOriginalUrl_shouldReturnStoredUrl() {
        when(redisService.get(ENCODED_ALIAS)).thenReturn(ORIGINAL_URL);

        String result = urlService.getOriginalUrl(ENCODED_ALIAS);

        assertEquals(ORIGINAL_URL, result);
        verify(redisService).get(ENCODED_ALIAS);
    }

    @Test
    void getOriginalUrl_shouldReturnNull_whenAliasNotFound() {
        when(redisService.get(ENCODED_ALIAS)).thenReturn(null);

        String result = urlService.getOriginalUrl(ENCODED_ALIAS);

        assertNull(result);
        verify(redisService).get(ENCODED_ALIAS);
    }

    @Test
    void shortenUrl_shouldCallDependenciesInOrder() {
        when(redisService.generateKey()).thenReturn(GENERATED_ID);

        try (MockedStatic<Base62Encoder> mockedEncoder = mockStatic(Base62Encoder.class)) {
            mockedEncoder.when(() -> Base62Encoder.encode(GENERATED_ID))
                    .thenReturn(ENCODED_ALIAS);

            urlService.shortenUrl(ORIGINAL_URL);

            verify(redisService).generateKey();
            verify(redisService).save(ENCODED_ALIAS, ORIGINAL_URL);
            mockedEncoder.verify(() -> Base62Encoder.encode(GENERATED_ID));
        }
    }
}
