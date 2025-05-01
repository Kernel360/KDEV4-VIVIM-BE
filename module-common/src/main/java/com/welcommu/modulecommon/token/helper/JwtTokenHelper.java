package com.welcommu.modulecommon.token.helper;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.modulecommon.token.dto.TokenDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final Long refreshTokenPlusHour;

    @Autowired
    public JwtTokenHelper(Environment env) {

        String key = env.getProperty("token.secret.key");
        if (key == null || key.isEmpty()) {
            throw new CustomException(CustomErrorCode.SERVER_ERROR);
        }
        this.secretKey = key;

        String accessHourStr = env.getProperty("token.access-token.plus-hour");
        this.accessTokenPlusHour = (accessHourStr != null) ? Long.valueOf(accessHourStr) : 1L;
        log.info("Access Token 만료 시간: {}시간", this.accessTokenPlusHour);

        String refreshHourStr = env.getProperty("token.refresh-token.plus-hour");
        this.refreshTokenPlusHour = (refreshHourStr != null) ? Long.valueOf(refreshHourStr) : 12L;
        log.info("Refresh Token 만료 시간: {}시간", this.refreshTokenPlusHour);
    }

    // 입력받은 claims(사용자 정보 등)를 바탕으로 JWT Access Token 생성
    // 토큰 문자열과 만료 시간을 포함한 TokenDto 로 반환
    public TokenDto issueAccessToken(Map<String, Object> claims) {

        LocalDateTime expiredLocalDateTime = LocalDateTime.now().plusHours(accessTokenPlusHour);
        Date expiredAt = Date.from(expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

        claims.put("tokenType", "access");

        String jwtToken = Jwts.builder()
            .setClaims(claims)
            .setExpiration(expiredAt)
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
            .compact();

        log.info("생성된 Access Token: {}", jwtToken);
        log.info("Access Token 만료 시간: {}", expiredLocalDateTime);

        return new TokenDto(jwtToken, expiredLocalDateTime);
    }

    public TokenDto issueRefreshToken(Map<String, Object> claims) {

        LocalDateTime expiredLocalDateTime = LocalDateTime.now().plusHours(refreshTokenPlusHour);
        Date expiredAt = Date.from(expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

        claims.put("tokenType", "refresh");

        String jwtToken = Jwts.builder()
            .setClaims(claims)
            .setExpiration(expiredAt)
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
            .compact();

        log.info("Refresh Token 생성 완료, 만료 시간: {}", expiredLocalDateTime);
        return new TokenDto(jwtToken, expiredLocalDateTime);
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
