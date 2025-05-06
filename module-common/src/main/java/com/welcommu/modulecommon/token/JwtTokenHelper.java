package com.welcommu.modulecommon.token;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenHelper {

    private final String secretKey;

    private final Long accessTokenPlusHour;
    private final Long accessTokenPlusMinute;
    private final Long accessTokenPlusSecond;

    private final Long refreshTokenPlusHour;
    private final Long refreshTokenPlusMinute;
    private final Long refreshTokenPlusSecond;

    public JwtTokenHelper(Environment env) {

        String key = env.getProperty("token.secret.key");
        if (key == null || key.isEmpty()) {
            throw new CustomException(CustomErrorCode.SERVER_ERROR);
        }
        this.secretKey = key;

        // 2) 만료 기간 설정
        this.accessTokenPlusHour   = getLong(env, "token.access-token.plus-hour",   0L);
        this.accessTokenPlusMinute = getLong(env, "token.access-token.plus-minute", 0L);
        this.accessTokenPlusSecond = getLong(env, "token.access-token.plus-second",10L);

        this.refreshTokenPlusHour   = getLong(env, "token.refresh-token.plus-hour",   0L);
        this.refreshTokenPlusMinute = getLong(env, "token.refresh-token.plus-minute", 1L);
        this.refreshTokenPlusSecond = getLong(env, "token.refresh-token.plus-second", 0L);

        log.info("Access Token 만료: {}시간 {}분 {}초", accessTokenPlusHour, accessTokenPlusMinute, accessTokenPlusSecond);
        log.info("Refresh Token 만료: {}시간 {}분 {}초", refreshTokenPlusHour, refreshTokenPlusMinute, refreshTokenPlusSecond);
    }

    private Long getLong(Environment env, String key, Long defaultVal) {
        String v = env.getProperty(key);
        return (v != null && !v.isEmpty()) ? Long.valueOf(v) : defaultVal;
    }

    public TokenDto issueAccessToken(Map<String, Object> claims) {
        LocalDateTime exp = LocalDateTime.now()
            .plusHours(accessTokenPlusHour)
            .plusMinutes(accessTokenPlusMinute)
            .plusSeconds(accessTokenPlusSecond);
        Date expiry = Date.from(exp.atZone(ZoneId.systemDefault()).toInstant());

        claims.put("tokenType", "access");

        String jwt = Jwts.builder()
            .setClaims(claims)
            .setExpiration(expiry)
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
            .compact();

        return new TokenDto(jwt, exp);
    }

    public TokenDto issueRefreshToken(Map<String, Object> claims) {
        LocalDateTime exp = LocalDateTime.now()
            .plusHours(refreshTokenPlusHour)
            .plusMinutes(refreshTokenPlusMinute)
            .plusSeconds(refreshTokenPlusSecond);
        Date expiry = Date.from(exp.atZone(ZoneId.systemDefault()).toInstant());

        claims.put("tokenType", "refresh");

        String jwt = Jwts.builder()
            .setClaims(claims)
            .setExpiration(expiry)
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
            .compact();

        return new TokenDto(jwt, exp);
    }

    public Map<String, Object> validationTokenWithThrow(String token) {
        System.out.println("*******************************************************"+token);
        if (token == null || !token.contains(".")) {
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }
        try {
            var key = Keys.hmacShaKeyFor(secretKey.getBytes());
            var parser = Jwts.parserBuilder().setSigningKey(key).build();
            var result = parser.parseClaimsJws(token);
            return new HashMap<>(result.getBody());
        } catch (SignatureException e) {
            throw new CustomException(CustomErrorCode.INVALID_TOKEN, e);
        } catch (ExpiredJwtException e) {
            throw new CustomException(CustomErrorCode.EXPIRED_TOKEN, e);
        } catch (Exception e) {
            throw new CustomException(CustomErrorCode.SERVER_ERROR, e);
        }
    }

    public static String withBearer(String token) {
        return "Bearer " + token;
    }

    public static String withoutBearer(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return header;
    }
}