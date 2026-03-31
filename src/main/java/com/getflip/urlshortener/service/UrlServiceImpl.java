package com.getflip.urlshortener.service;

import com.getflip.urlshortener.util.Base62Encoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final RedisService redisService;

    @Override
    public String shortenUrl(String originalUrl) {
        long id = redisService.generateKey();

        String alias = Base62Encoder.encode(id);

        redisService.save(alias, originalUrl);

        return alias;
    }

    @Override
    public String getOriginalUrl(String alias) {
        return redisService.get(alias);
    }
}
