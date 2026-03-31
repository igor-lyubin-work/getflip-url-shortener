package com.getflip.urlshortener.service.url;

import com.getflip.urlshortener.service.redis.RedisService;
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
        String alias = Base62Encoder.generateRedisKey(originalUrl);

        redisService.save(alias, originalUrl);

        return alias;
    }

    @Override
    public String getOriginalUrl(String alias) {
        return redisService.get(alias);
    }
}
