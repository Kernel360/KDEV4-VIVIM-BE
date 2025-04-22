package com.welcommu.moduleapi.auth;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.modulecommon.token.dto.TokenDto;
import com.welcommu.modulecommon.token.helper.JwtTokenHelper;
import com.welcommu.moduledomain.token.RefreshTokenEntity;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.token.RefreshTokenRepository;
import com.welcommu.moduleservice.auth.dto.LoginRequest;
import com.welcommu.moduleservice.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "인증 인가 API", description = "로그인, 로그아웃, 토큰 재발급 등 인증 및 권한과 관련된 기능을 제공합니다.")
public class AuthController {

    private final JwtTokenHelper jwtTokenHelper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository; // Refresh Token을 저장할 레포지토리

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호를 사용해 로그인하고 Access Token을 발급받습니다.")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        try {
            String email = request.getEmail();
            String password = request.getPassword();

            User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("Invalid email or password");
            }

            Map<String, Object> claims = new HashMap<>();
            claims.put("email", user.getEmail());  // 클레임에 이메일 정보 추가
            claims.put("userId", user.getId());    // 클레임에 사용자 ID 추가
            claims.put("role", user.getRole());

            // 고유한 jti (JWT ID) 추가
            String tokenId = UUID.randomUUID().toString();
            claims.put("jti", tokenId);

            TokenDto accessToken = jwtTokenHelper.issueAccessToken(claims);
            TokenDto refreshToken = jwtTokenHelper.issueRefreshToken(claims);

            // Refresh Token 저장 (사용자 ID, 토큰 ID(jti), 토큰 값 저장)
            saveRefreshToken(user.getId(), tokenId, refreshToken.getToken(), refreshToken.getExpiredAt());

            // 토큰을 JSON 형식으로 반환
            Map<String, String> response = new HashMap<>();
            response.put("access_token", "Bearer " + accessToken.getToken());
            response.put("refresh_token", "Bearer " + refreshToken.getToken());

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("message", "Internal server error", "error", e.getMessage()));
        }
    }

    @GetMapping("/user")
    @Operation(summary = "로그인한 사용자 확인")
    public ResponseEntity<Map<String, String>> getUserInfo(
        @AuthenticationPrincipal String username) {
        Map<String, String> response = new HashMap<>();
        response.put("authenticatedUser", username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Access Token 재발급", description = "Refresh Token을 사용하여 새로운 Access Token과 Refresh Token을 발급받습니다.")
    public ResponseEntity<Map<String, String>> refreshAccessToken(
        @RequestParam String refreshToken) {
        try {
            // Bearer 접두사 제거
            if (refreshToken.startsWith("Bearer ")) {
                refreshToken = refreshToken.substring(7);
            }

            // Refresh Token 검증
            Map<String, Object> claims = jwtTokenHelper.validationTokenWithThrow(refreshToken);

            // 토큰 ID(jti)와 사용자 ID 추출
            String tokenId = (String) claims.get("jti");
            Long userId = ((Integer) claims.get("userId")).longValue();

            // DB에서 저장된 Refresh Token 정보 확인
            RefreshTokenEntity storedToken = refreshTokenRepository.findByUserIdAndTokenId(userId,
                    tokenId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.INVALID_TOKEN));

            // 토큰 일치 확인
            if (!storedToken.getToken().equals(refreshToken)) {
                // 토큰 불일치 - 해당 토큰 삭제 및 예외 발생
                refreshTokenRepository.delete(storedToken);
                throw new CustomException(CustomErrorCode.INVALID_TOKEN);
            }

            // 사용된 Refresh Token 삭제
            refreshTokenRepository.delete(storedToken);

            // 새로운 토큰 ID 생성
            String newTokenId = UUID.randomUUID().toString();
            claims.put("jti", newTokenId);

            // 새 Access Token 발급
            TokenDto newAccessToken = jwtTokenHelper.issueAccessToken(claims);

            // 새 Refresh Token 발급 (Token Rotation)
            TokenDto newRefreshToken = jwtTokenHelper.issueRefreshToken(claims);

            // 새 Refresh Token 저장
            saveRefreshToken(userId, newTokenId, newRefreshToken.getToken(),
                newRefreshToken.getExpiredAt());

            // 새 토큰 반환
            Map<String, String> response = new HashMap<>();
            response.put("access_token", "Bearer " + newAccessToken.getToken());
            response.put("refresh_token", "Bearer " + newRefreshToken.getToken());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("message", "Internal server error", "error", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "Refresh Token을 만료 처리하여 로그아웃을 수행합니다.")
    public ResponseEntity<?> logout(@RequestParam String refreshToken) {
        try {
            if (refreshToken.startsWith("Bearer ")) {
                refreshToken = refreshToken.substring(7);
            }

            // 토큰 검증 및 클레임 추출
            Map<String, Object> claims = jwtTokenHelper.validationTokenWithThrow(refreshToken);
            String tokenId = (String) claims.get("jti");

            // DB에서 해당 토큰 삭제
            refreshTokenRepository.deleteByTokenId(tokenId);

            return ResponseEntity.ok(Map.of("message", "Successfully logged out"));
        } catch (Exception e) {
            // 실패해도 로그아웃은 성공했다고 간주
            return ResponseEntity.ok(Map.of("message", "Successfully logged out"));
        }
    }

    // Refresh Token을 DB에 저장하는 메소드
    private void saveRefreshToken(Long userId, String tokenId, String token,
        java.time.LocalDateTime expiresAt) {
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setUserId(userId);
        entity.setTokenId(tokenId);
        entity.setToken(token);
        entity.setExpiresAt(expiresAt);
        refreshTokenRepository.save(entity);
    }
}