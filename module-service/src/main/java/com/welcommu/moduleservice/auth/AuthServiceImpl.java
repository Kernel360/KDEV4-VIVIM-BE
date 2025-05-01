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

    @Override
    public LoginResponse createToken(LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        User user = userService.getUserByEmail(email)
            .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        UserResponse userDto = UserResponse.from(user);

        // JWT Claims 구성
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userDto.getEmail());
        claims.put("userId", userDto.getId());
        claims.put("role", userDto.getCompanyRole());

        // JWT ID 생성
        String tokenId = UUID.randomUUID().toString();
        claims.put("jti", tokenId);

        // 액세스 / 리프레시 토큰 발급
        TokenDto accessToken = jwtTokenHelper.issueAccessToken(claims);
        TokenDto refreshToken = jwtTokenHelper.issueRefreshToken(claims);

        // Redis에 Refresh Token 저장
        refreshTokenService.save(
            user.getId(),
            refreshToken.getToken(),
            calcExpireSeconds(refreshToken.getExpiredAt())
        );

        // "Bearer " 접두사 일원화
        String bearerAccessToken  = JwtTokenHelper.withBearer(accessToken.getToken());
        String bearerRefreshToken = JwtTokenHelper.withBearer(refreshToken.getToken());

        return LoginResponse.builder()
            .accessToken(bearerAccessToken)
            .refreshToken(bearerRefreshToken)
            .build();
    }

    @Override
    public LoginResponse reIssueToken(String refreshTokenHeader) {
        log.info("리프레시 토큰으로 액세스 재발급 시도");

        // Bearer 접두사 제거
        String refreshToken = JwtTokenHelper.withoutBearer(refreshTokenHeader);

        // Refresh Token 검증
        Map<String, Object> claims = jwtTokenHelper.validationTokenWithThrow(refreshToken);
        long userId = parseUserId(claims.get("userId"));

        // Redis에서 저장된 토큰 확인 및 회전
        if (!refreshTokenService.isValid(userId, refreshToken)) {
            refreshTokenService.delete(userId);
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }
        refreshTokenService.delete(userId);

        // 새로운 토큰 ID 발급 및 claims 갱신
        String newTokenId = UUID.randomUUID().toString();
        claims.put("jti", newTokenId);

        TokenDto newAccessToken  = jwtTokenHelper.issueAccessToken(claims);
        TokenDto newRefreshToken = jwtTokenHelper.issueRefreshToken(claims);

        refreshTokenService.save(
            userId,
            newRefreshToken.getToken(),
            calcExpireSeconds(newRefreshToken.getExpiredAt())
        );

        String bearerAccessToken  = JwtTokenHelper.withBearer(newAccessToken.getToken());
        String bearerRefreshToken = JwtTokenHelper.withBearer(newRefreshToken.getToken());

        return LoginResponse.builder()
            .accessToken(bearerAccessToken)
            .refreshToken(bearerRefreshToken)
            .build();
    }

    @Override
    public void deleteToken(String refreshTokenHeader) {
        // Bearer 접두사 제거
        String refreshToken = JwtTokenHelper.withoutBearer(refreshTokenHeader);

        // 토큰 검증 및 클레임 추출
        Map<String, Object> claims = jwtTokenHelper.validationTokenWithThrow(refreshToken);
        long userId = parseUserId(claims.get("userId"));

        // Redis에서 해당 사용자 토큰 삭제
        refreshTokenService.delete(userId);
    }

    /**
     * 여러 타입의 raw userId(Object)를 long으로 변환
     */
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

    /**
     * 토큰 DTO 만료 시간을 초 단위 만료 기간으로 계산
     */
    private long calcExpireSeconds(LocalDateTime expiresAt) {
        return Duration.between(LocalDateTime.now(), expiresAt).getSeconds();
    }
}