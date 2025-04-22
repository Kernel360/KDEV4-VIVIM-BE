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
public class AuthService {

    private final JwtTokenHelper jwtTokenHelper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

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
        // refreshToken.getExpiredAt()이 LocalDateTime이면 Duration 계산 필요
        long expireSeconds = java.time.Duration.between(
            java.time.LocalDateTime.now(),
            refreshToken.getExpiredAt()
        ).getSeconds();

        refreshTokenService.save(user.getId(), refreshToken.getToken(), expireSeconds);

        // 응답 반환
        return LoginResponse.builder()
            .accessToken("Bearer " + accessToken.getToken())
            .refreshToken("Bearer " + refreshToken.getToken())
            .build();
    }

    public LoginResponse reIssueToken(String refreshToken) {
        log.info("리프레시 토큰으로 액세스 재발급 시도");

        // Bearer 접두사 제거
        refreshToken = getRefreshTokenWithoutBearer(refreshToken);

        // Refresh Token 검증
        Map<String, Object> claims = jwtTokenHelper.validationTokenWithThrow(refreshToken);

        // 클레임에서 사용자 정보 추출
        Long userId = ((Integer) claims.get("userId")).longValue(); // int -> long

        // Redis에서 저장된 토큰 확인
        if (!refreshTokenService.isValid(userId, refreshToken)) {
            refreshTokenService.delete(userId); // 혹시라도 남아있으면 삭제
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }

        // 기존 토큰 삭제 (Token Rotation)
        refreshTokenService.delete(userId);

        // 새로운 토큰 ID 발급 및 claims 갱신
        String newTokenId = UUID.randomUUID().toString();
        claims.put("jti", newTokenId);

        // 새 Access, Refresh 토큰 발급
        TokenDto newAccessToken = jwtTokenHelper.issueAccessToken(claims);
        TokenDto newRefreshToken = jwtTokenHelper.issueRefreshToken(claims);

        // Redis에 새 리프레시 토큰 저장
        long expireSeconds = Duration.between(
            LocalDateTime.now(),
            newRefreshToken.getExpiredAt()
        ).getSeconds();
        refreshTokenService.save(userId, newRefreshToken.getToken(), expireSeconds);

        return LoginResponse.builder()
            .accessToken("Bearer " + newAccessToken.getToken())
            .refreshToken("Bearer " + newRefreshToken.getToken())
            .build();
    }

    public void deleteToken(String refreshToken){
        refreshToken = getRefreshTokenWithoutBearer(refreshToken);

        // 토큰 검증 및 클레임 추출
        Map<String, Object> claims = jwtTokenHelper.validationTokenWithThrow(refreshToken);
        Object rawUserId = claims.get("userId");

        long userId;

        try {
            if (rawUserId instanceof Integer i) {
                userId = i.longValue();
            } else if (rawUserId instanceof Long l) {
                userId = l;
            } else if (rawUserId instanceof String s) {
                userId = Long.parseLong(s);
            } else {
                log.error("지원하지 않는 userId 타입: {}", rawUserId);
                throw  new CustomException(CustomErrorCode.INVALID_USERID_TYPE);
            }
        } catch (Exception e) {
            log.error("JWT userId 파싱 실패: {}", rawUserId, e);
            throw  new CustomException(CustomErrorCode.INVALID_TOKEN);
        }
        // Redis에서 해당 사용자 토큰 삭제
        refreshTokenService.delete(userId);


    }

    private String getRefreshTokenWithoutBearer(String refreshToken) {
        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }
        return refreshToken;
    }

}
