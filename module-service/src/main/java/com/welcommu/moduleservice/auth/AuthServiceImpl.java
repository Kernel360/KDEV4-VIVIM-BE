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
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final JwtTokenHelper jwtTokenHelper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    @Override
    public LoginResponse createToken(LoginRequest request) {
        User user = authenticateUser(request.getEmail(), request.getPassword());
        UserResponse userDto = UserResponse.from(user);

        // JWT 토큰에 담을 사용자 정보(Claims)를 UserResponse 객체를 기반으로 생성
        Map<String, Object> claims = createClaims(userDto);

        TokenDto accessToken = jwtTokenHelper.issueAccessToken(claims);
        TokenDto refreshToken = jwtTokenHelper.issueRefreshToken(claims);

        saveRefreshToken(user.getId(), refreshToken);

        return buildLoginResponse(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public LoginResponse reIssueToken(String refreshTokenHeader) {
        String refreshToken = JwtTokenHelper.withoutBearer(refreshTokenHeader);
        Map<String, Object> claims = jwtTokenHelper.validationTokenWithThrow(refreshToken);

        validateRefreshTokenType(claims);

        long userId = parseUserId(claims.get("userId"));

        verifyAndRotateRefreshToken(userId, refreshToken);

        claims.put("jti", UUID.randomUUID().toString());

        TokenDto newAccessToken = jwtTokenHelper.issueAccessToken(claims);
        TokenDto newRefreshToken = jwtTokenHelper.issueRefreshToken(claims);

        saveRefreshToken(userId, newRefreshToken);

        return buildLoginResponse(newAccessToken, newRefreshToken);
    }

    @Override
    public void deleteToken(String refreshTokenHeader) {
        String refreshToken = JwtTokenHelper.withoutBearer(refreshTokenHeader);
        Map<String, Object> claims = jwtTokenHelper.validationTokenWithThrow(refreshToken);
        long userId = parseUserId(claims.get("userId"));

        // Redis 에서 해당 사용자 토큰 삭제
        refreshTokenService.delete(userId);
    }

    // ===================== ↓ Internal Methods ↓ =====================

    private User authenticateUser(String email, String password) {
        User user = userService.getUserByEmail(email)
            .orElseThrow(() -> new CustomException(CustomErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(CustomErrorCode.INVALID_CREDENTIALS);
        }
        return user;
    }

    private Map<String, Object> createClaims(UserResponse userDto) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userDto.getEmail());
        claims.put("userId", userDto.getId());
        claims.put("role", userDto.getCompanyRole());
        claims.put("jti", UUID.randomUUID().toString());
        return claims;
    }

    private void saveRefreshToken(Long userId, TokenDto refreshToken) {
        refreshTokenService.save(
            userId,
            refreshToken.getToken(),
            calcExpireSeconds(refreshToken.getExpiredAt())
        );
    }

    private void verifyAndRotateRefreshToken(long userId, String oldToken) {
        if (!refreshTokenService.isValid(userId, oldToken)) {
            refreshTokenService.delete(userId);
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }
        refreshTokenService.delete(userId); // 회전 처리
    }

    private void validateRefreshTokenType(Map<String, Object> claims) {
        String tokenType = (String) claims.get("tokenType");
        if (!"refresh".equals(tokenType)) {
            throw new CustomException(CustomErrorCode.INVALID_TOKEN_TYPE);
        }
    }

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

    private long calcExpireSeconds(LocalDateTime expiresAt) {
        if (expiresAt == null) throw new CustomException(CustomErrorCode.SERVER_ERROR);
        long seconds = Duration.between(LocalDateTime.now(), expiresAt).getSeconds();
        return Math.max(seconds, 1);
    }

    private LoginResponse buildLoginResponse(TokenDto accessToken, TokenDto refreshToken) {
        return LoginResponse.builder()
            .accessToken(JwtTokenHelper.withBearer(accessToken.getToken()))
            .refreshToken(JwtTokenHelper.withBearer(refreshToken.getToken()))
            .build();
    }
}