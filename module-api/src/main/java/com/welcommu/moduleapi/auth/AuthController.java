package com.welcommu.moduleapi.auth;


import com.welcommu.moduleservice.auth.AuthService;
import com.welcommu.moduleservice.auth.dto.LoginRequest;
import com.welcommu.moduleservice.auth.dto.LoginResponse;
import com.welcommu.moduleservice.auth.dto.RefreshTokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "인증 인가 API", description = "로그인, 로그아웃, 토큰 재발급 등 인증 및 권한과 관련된 기능을 제공합니다.")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호를 사용해 로그인하고 Access Token을 발급받습니다.")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.createToken(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401)
                .body(Map.of("message", "Invalid credentials"));
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
    public ResponseEntity<?> refreshAccessToken(@RequestBody RefreshTokenRequest refreshToken) {
        try {
            LoginResponse response = authService.reIssueToken(refreshToken.getRefreshToken());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("message", "Internal server error", "error", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "Refresh Token을 만료 처리하여 로그아웃을 수행합니다.")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequest refreshToken) {
        try {
            authService.deleteToken(refreshToken.getRefreshToken());
            return ResponseEntity.ok(Map.of("message", "Successfully logged out"));
        } catch (Exception e) {
            // 실패해도 로그아웃은 성공했다고 간주
            return ResponseEntity.ok(Map.of("message", "Successfully logged out"));
        }
    }
}