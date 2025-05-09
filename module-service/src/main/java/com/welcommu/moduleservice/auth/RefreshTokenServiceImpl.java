package com.welcommu.moduleservice.auth;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    private String key(Long userId) {
        return "RT:" + userId;
    }

    @Override
    public void save(Long userId, String token, long expireSeconds) {
        redisTemplate.opsForValue()
            .set(key(userId), token, Duration.ofSeconds(expireSeconds));
    }

    @Override
    public boolean isValid(Long userId, String token) {
        String stored = redisTemplate.opsForValue()
            .get(key(userId));
        return token.equals(stored);
    }

    @Override
    public void delete(Long userId) {
        redisTemplate.delete(key(userId));
    }

    @Override
    public String get(Long userId) {
        return redisTemplate.opsForValue()
            .get(key(userId));
    }
}