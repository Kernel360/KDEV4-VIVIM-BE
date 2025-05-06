package com.welcommu.moduleservice.auth;

public interface RefreshTokenService {
    void save(Long userId, String token, long expireSeconds);
    boolean isValid(Long userId, String token);
    void delete(Long userId);
    String get(Long userId);
}