package com.welcommu.moduleservice.auth;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.modulecommon.token.dto.TokenDto;
import com.welcommu.modulecommon.token.helper.JwtTokenHelper;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleservice.auth.dto.LoginRequest;
import com.welcommu.moduleservice.auth.dto.LoginResponse;
import com.welcommu.moduleservice.redis.RefreshTokenService;
import com.welcommu.moduleservice.user.UserService;
import com.welcommu.moduleservice.user.dto.UserResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final JwtTokenHelper jwtTokenHelper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    private Map<String, Object> createClaims(UserResponse userDto) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userDto.getEmail());
        claims.put("userId", userDto.getId());
        claims.put("role", userDto.getCompanyRole());
        claims.put("jti", UUID.randomUUID().toString());
        return claims;
    }

    @Override
    public LoginResponse createToken(LoginRequest request) {

        User user = authenticateUser(request.getEmail(), request.getPassword());
        UserResponse userDto = UserResponse.from(user);

        // JWT 토큰에 담을 사용자 정보(Claims)를 UserResponse 객체를 기반으로 생성
        Map<String, Object> claims = createClaims(userDto);

        TokenDto accessToken  = jwtTokenHelper.issueAccessToken(claims);
        TokenDto refreshToken = jwtTokenHelper.issueRefreshToken(claims);

        saveRefreshToken(user.getId(), refreshToken);

        return LoginResponse.builder()
            .accessToken(JwtTokenHelper.withBearer(accessToken.getToken()))
            .refreshToken(JwtTokenHelper.withBearer(refreshToken.getToken()))
            .build();
    }

    @Override
    public LoginResponse reIssueToken(String refreshTokenHeader) {
        String refreshToken = JwtTokenHelper.withoutBearer(refreshTokenHeader);
        Map<String, Object> claims = jwtTokenHelper.validationTokenWithThrow(refreshToken);

        validateRefreshToken(claims);

        long userId = parseUserId(claims.get("userId"));
        rotateRefreshToken(userId, refreshToken);

        claims.put("jti", UUID.randomUUID().toString());

        TokenDto newAccessToken  = jwtTokenHelper.issueAccessToken(claims);
        TokenDto newRefreshToken = jwtTokenHelper.issueRefreshToken(claims);

        saveRefreshToken(userId, newRefreshToken);

        return LoginResponse.builder()
            .accessToken(JwtTokenHelper.withBearer(newAccessToken.getToken()))
            .refreshToken(JwtTokenHelper.withBearer(newRefreshToken.getToken()))
            .build();
    }

    @Override
    public void deleteToken(String refreshTokenHeader) {

        String refreshToken = JwtTokenHelper.withoutBearer(refreshTokenHeader);

        Map<String, Object> claims = jwtTokenHelper.validationTokenWithThrow(refreshToken);
        long userId = parseUserId(claims.get("userId"));

        // Redis 에서 해당 사용자 토큰 삭제
        refreshTokenService.delete(userId);
    }

    private void saveRefreshToken(Long userId, TokenDto refreshToken) {
        refreshTokenService.save(
            userId,
            refreshToken.getToken(),
            calcExpireSeconds(refreshToken.getExpiredAt())
        );
    }

    private void rotateRefreshToken(long userId, String oldRefreshToken) {
        if (!refreshTokenService.isValid(userId, oldRefreshToken)) {
            refreshTokenService.delete(userId);
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }
        refreshTokenService.delete(userId);
    }

    private void validateRefreshToken(Map<String, Object> claims) {
        String tokenType = (String) claims.get("tokenType");
        if (!"refresh".equals(tokenType)) {
            throw new CustomException(CustomErrorCode.INVALID_TOKEN_TYPE);
        }
    }

    // 이메일 또는 비밀번호 검증
    private User authenticateUser(String email, String password) {
        User user = userService.getUserByEmail(email)
            .orElseThrow(() -> new CustomException(CustomErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(CustomErrorCode.INVALID_CREDENTIALS);
        }
        return user;
    }

    // 여러 타입의 raw userId(Object)를 long 으로 변환
    private long parseUserId(Object raw) {

        if (raw instanceof Integer) {
            return ((Integer) raw).longValue();
        } else if (raw instanceof Long) {
            return (Long) raw;
        } else if (raw instanceof String) {
            return Long.parseLong((String) raw);
        }
        log.error("지원하지 않는 userId 타입: {}", raw);
        throw new CustomException(CustomErrorCode.INVALID_USERID_TYPE);
    }

    // 토큰 DTO 만료 시간을 초 단위 만료 기간으로 계산
    private long calcExpireSeconds(LocalDateTime expiresAt) {
        if (expiresAt == null) throw new CustomException(CustomErrorCode.SERVER_ERROR);
        long seconds = Duration.between(LocalDateTime.now(), expiresAt).getSeconds();
        return Math.max(seconds, 1); // 최소 1초 보장
    }
}