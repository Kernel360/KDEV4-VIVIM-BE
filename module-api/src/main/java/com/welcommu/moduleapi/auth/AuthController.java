package com.welcommu.moduleapi.auth;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.modulecommon.util.TokenCookieUtil;
import com.welcommu.moduleservice.auth.AuthService;
import com.welcommu.moduleservice.auth.dto.LoginRequest;
import com.welcommu.moduleservice.auth.dto.LoginResponse;
import com.welcommu.moduleservice.auth.dto.RefreshTokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "인증 인가 API", description = "로그인, 로그아웃, 토큰 재발급 등 인증 및 권한과 관련된 기능을 제공합니다.")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호를 사용해 로그인하고 Access Token 을 발급받습니다.")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = authService.createToken(request);
        TokenCookieUtil.addTokenCookies(
            response,
            loginResponse.getAccessToken().replace("Bearer ", ""),
            loginResponse.getRefreshToken().replace("Bearer ", "")
        );
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.OK.value(), "로그인을 성공했습니다."));
    }

    @GetMapping("/user")
    @Operation(summary = "로그인한 사용자 확인")
    public ResponseEntity<ApiResponse> getUserInfo(@AuthenticationPrincipal String username) {
        Map<String, String> response = new HashMap<>();
        response.put("authenticatedUser", username);
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.OK.value(), "사용자 정보를 조회했습니다.", response));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Access Token 재발급", description = "Refresh Token을 사용하여 새로운 Access Token과 Refresh Token을 발급받습니다.")
    public ResponseEntity<ApiResponse> refreshAccessToken(@RequestBody RefreshTokenRequest refreshToken, HttpServletResponse response) {
        LoginResponse loginResponse = authService.reIssueToken(refreshToken.getRefreshToken());
        TokenCookieUtil.addTokenCookies(
            response,
            loginResponse.getAccessToken().replace("Bearer ", ""),
            loginResponse.getRefreshToken().replace("Bearer ", "")
        );
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.OK.value(), "토큰을 재발급했습니다."));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "Refresh Token 을 만료 처리하여 로그아웃을 수행합니다.")
    public ResponseEntity<ApiResponse> logout(@RequestBody RefreshTokenRequest refreshToken) {
        try {
            authService.deleteToken(refreshToken.getRefreshToken());
        } catch (Exception e) {
            log.warn("Refresh Token 삭제 실패", e);
        }
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.OK.value(), "로그아웃을 성공했습니다."));
    }
}