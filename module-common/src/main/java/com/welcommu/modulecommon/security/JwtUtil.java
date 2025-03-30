package com.welcommu.modulecommon.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date; // java.util.Date 사용
import java.time.Instant;

@Component
@Slf4j
public class JwtUtil {

//    @Value("${token.secret.key}")
//    private String secretKey;  // application.yml에서 secretKey를 주입받음
    private String secretKey = "cgxz5nCh3+4g1p5pLlCeDft6gT4zqPdc9Vr2vym8NE0=";

    private long expirationTime = 3600000; // 1 hour in milliseconds

    // 🔹 키 생성 (HMAC-SHA 방식)
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // 🔹 JWT 생성
    public String generateToken(String username) {
        // 현재 시간 가져오기
        long currentTimeMillis = System.currentTimeMillis();

        String token = Jwts.builder()
                .setSubject(username) // 사용자 정보
                .setIssuedAt(new Date(currentTimeMillis)) // 발행 시간
                .setExpiration(new Date(currentTimeMillis + expirationTime)) // 만료 시간
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 서명
                .compact(); // JWT 반환

        // 생성된 토큰 출력
        log.info("생성된 JWT 토큰: " + token);

        return token;
    }


    // 🔹 JWT에서 사용자 이름 추출
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // 🔹 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        Date expirationDate = getClaims(token).getExpiration();
        return expirationDate.before(new Date()); // 만료 시간을 현재 시간과 비교
    }

    public boolean validateToken(String token) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername != null && !isTokenExpired(token));
    }


    // 🔹 토큰에서 Claims(정보) 추출
    private Claims getClaims(String token) {
        log.info("JWT Token: " + token);  // 로그로 토큰 확인
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
