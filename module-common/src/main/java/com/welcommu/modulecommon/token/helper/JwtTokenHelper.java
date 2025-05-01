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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenHelper {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenHelper.class);

    private final String secretKey;
    private final Long accessTokenPlusHour;
    private final Long refreshTokenPlusHour;

    @Autowired
    public JwtTokenHelper(Environment env) {
        String key = env.getProperty("token.secret.key");
        if (key == null || key.isEmpty()) {
            logger.error("토큰 시크릿 키가 설정되지 않았습니다.");
            throw new IllegalStateException("토큰 시크릿 키가 설정되지 않았습니다.");
        }
        this.secretKey = key;

        String accessHourStr = env.getProperty("token.access-token.plus-hour");
        this.accessTokenPlusHour = (accessHourStr != null) ? Long.valueOf(accessHourStr) : 1L;
        logger.info("Access Token 만료 시간: {}시간", this.accessTokenPlusHour);

        String refreshHourStr = env.getProperty("token.refresh-token.plus-hour");
        this.refreshTokenPlusHour = (refreshHourStr != null) ? Long.valueOf(refreshHourStr) : 12L;
        logger.info("Refresh Token 만료 시간: {}시간", this.refreshTokenPlusHour);
    }

    public TokenDto issueAccessToken(Map<String, Object> data) {
        LocalDateTime expiredLocalDateTime = LocalDateTime.now().plusHours(accessTokenPlusHour);
        Date expiredAt = Date.from(expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

        // HMAC-SHA256 알고리즘으로 서명
        String jwtToken = Jwts.builder()
            .setClaims(data) // 사용자 정보 및 클레임
            .setExpiration(expiredAt) // 만료 시간 설정
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256) // 서명
            .compact();
        logger.info("생성된 Access Token: {}", jwtToken);
        logger.info("Access Token 만료 시간: {}", expiredLocalDateTime);

        return new TokenDto(jwtToken, expiredLocalDateTime);
    }

    public TokenDto issueRefreshToken(Map<String, Object> data) {
        LocalDateTime expiredLocalDateTime = LocalDateTime.now().plusHours(refreshTokenPlusHour);
        Date expiredAt = Date.from(expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

        // HMAC-SHA256 알고리즘으로 서명
        String jwtToken = Jwts.builder()
            .setClaims(data)
            .setExpiration(expiredAt)
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
            .compact();

        logger.info("Refresh Token 생성 완료, 만료 시간: {}", expiredLocalDateTime);
        return new TokenDto(jwtToken, expiredLocalDateTime);
    }

    public Map<String, Object> validationTokenWithThrow(String token) {
        if (token == null || !token.contains(".")) {
            logger.error("잘못된 JWT 토큰 형식. 토큰은 반드시 '.' 구분자를 포함해야 합니다.");
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }

        logger.info("받은 JWT 토큰: {}", token); // 받은 토큰 값을 로그로 확인

        try {
            var key = Keys.hmacShaKeyFor(secretKey.getBytes());
            var parser = Jwts.parserBuilder().setSigningKey(key).build();
            var result = parser.parseClaimsJws(token);

            logger.info("토큰 검증 성공, 만료 시간: {}", result.getBody().getExpiration());

            return new HashMap<>(result.getBody());
        } catch (SignatureException e) {
            logger.error("토큰 서명 오류: {}", e.getMessage());
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            logger.error("토큰 만료: {}", e.getMessage());
            throw new CustomException(CustomErrorCode.EXPIRED_TOKEN, e);
        } catch (MalformedJwtException e) {
            logger.error("잘못된 JWT 토큰 형식: {}", e.getMessage());
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        } catch (Exception e) {
            logger.error("토큰 검증 기타 오류: {}", e.getMessage());
            throw new CustomException(CustomErrorCode.SERVER_ERROR, e);
        }
    }

    /** token 문자열에 "Bearer " 접두사를 붙입니다. */
    public static String withBearer(String token) {
        return "Bearer " + token;
    }

    /** "Bearer " 접두사가 붙은 헤더에서 토큰만 분리합니다. */
    public static String withoutBearer(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return header;
    }
}
