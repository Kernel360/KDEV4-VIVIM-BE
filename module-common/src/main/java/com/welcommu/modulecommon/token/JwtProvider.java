package com.welcommu.modulecommon.token;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtProvider {

    private final String secretKey;
    private final JwtProperties props;

    public JwtProvider(
        @Value("${token.secret.key}") String secretKey,
        JwtProperties props
    ) {
        if (secretKey == null || secretKey.isBlank()) {
            throw new CustomException(CustomErrorCode.SERVER_ERROR);
        }
        this.secretKey = secretKey;
        this.props = props;

        log.info("Access Token TTL: {}초", props.getAccessTokenTtl());
        log.info("Refresh Token TTL: {}초", props.getRefreshTokenTtl());
    }

    public JwtDto issueAccessToken(Map<String, Object> claims) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime exp  = now.plusSeconds(props.getAccessTokenTtl());
        Date expiryDate    = Date.from(exp.atZone(ZoneId.systemDefault()).toInstant());

        claims.put("tokenType", "access");

        String jwt = Jwts.builder()
            .setClaims(claims)
            .setExpiration(expiryDate)
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
            .compact();

        return JwtDto.builder()
            .token(jwt)
            .expiredAt(exp)
            .build();
    }

    public JwtDto issueRefreshToken(Map<String, Object> claims) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime exp  = now.plusSeconds(props.getRefreshTokenTtl());
        Date expiryDate    = Date.from(exp.atZone(ZoneId.systemDefault()).toInstant());

        claims.put("tokenType", "refresh");

        String jwt = Jwts.builder()
            .setClaims(claims)
            .setExpiration(expiryDate)
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
            .compact();

        return JwtDto.builder()
            .token(jwt)
            .expiredAt(exp)
            .build();
    }
    public Map<String, Object> validationTokenWithThrow(String token) {
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