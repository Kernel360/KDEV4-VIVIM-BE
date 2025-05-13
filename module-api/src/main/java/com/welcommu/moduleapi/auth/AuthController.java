package com.welcommu.moduleapi.auth;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.modulecommon.token.JwtProvider;
import com.welcommu.modulecommon.util.JwtUtil;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduleservice.auth.AuthService;
import com.welcommu.moduleservice.auth.dto.LoginRequest;
import com.welcommu.moduleservice.auth.dto.LoginResponse;
import com.welcommu.moduleservice.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "인증 인가 API", description = "로그인, 로그아웃, 토큰 재발급 등 인증 및 권한과 관련된 기능을 제공합니다.")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<ApiResponse> login(
        @RequestBody LoginRequest request,
        HttpServletResponse response
    ) {
        LoginResponse tokens = authService.createToken(request);

        jwtUtil.addTokenCookies(
            response,
            tokens.getAccessToken().replace("Bearer ", ""),
            tokens.getRefreshToken().replace("Bearer ", "")
        );

        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.OK.value(), "로그인을 성공했습니다."));
    }

    @GetMapping("/user")
    @Operation(summary = "로그인한 사용자 확인", description = "현재 인증된 사용자의 상세 정보를 반환합니다.")
    public ResponseEntity<ApiResponse> getUserInfo(
        @AuthenticationPrincipal AuthUserDetailsImpl principal
    ) {
        if (principal == null) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse(HttpStatus.UNAUTHORIZED.value(), "인증되지 않은 상태입니다."));
        }

        UserResponse dto = UserResponse.from(principal.getUser());

        return ResponseEntity.ok(
            new ApiResponse(HttpStatus.OK.value(), "사용자 정보를 조회했습니다.", dto)
        );
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Access Token 재발급", description = "쿠키에 담긴 Refresh Token을 사용해 새로운 Access·Refresh Token을 발급합니다.")
    public ResponseEntity<ApiResponse> refreshAccessToken(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        log.info("백엔드 함수 호출: refreshAccessToken");
        String rawToken = Optional.ofNullable(request.getCookies())
            .flatMap(cookies -> Arrays.stream(cookies)
                .filter(c -> "refreshToken".equals(c.getName()))
                .findFirst()
            )
            .map(Cookie::getValue)
            .orElseThrow(() -> new CustomException(CustomErrorCode.INVALID_TOKEN));
        log.debug("refreshToken rawCookieValue = {}", rawToken);


        LoginResponse loginResponse = authService.reIssueToken(rawToken);

        jwtUtil.addTokenCookies(
            response,
            loginResponse.getAccessToken().replace("Bearer ", ""),
            loginResponse.getRefreshToken().replace("Bearer ", "")
        );

        return ResponseEntity.ok(
            new ApiResponse(HttpStatus.OK.value(), "토큰을 재발급했습니다.")
        );
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "쿠키에 담긴 Refresh Token을 만료 처리합니다.")
    public ResponseEntity<ApiResponse> logout(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        // 1) 쿠키에서 refreshToken 꺼내기
        String rawToken = Optional.ofNullable(request.getCookies())
            .flatMap(cookies -> Arrays.stream(cookies)
                .filter(c -> "refreshToken".equals(c.getName()))
                .findFirst()
            )
            .map(Cookie::getValue)
            .orElse(null);

        if (rawToken != null) {
            String refreshToken = JwtProvider.withoutBearer(rawToken);
            try {
                authService.deleteToken(refreshToken);
            } catch (Exception e) {
                log.warn("Refresh Token 삭제 실패", e);
            }
        }

        // 2) 클라이언트 쿠키 만료 처리
        deleteCookie(response, "refreshToken");
        deleteCookie(response, "accessToken");  // AT 쿠키도 동일하게 처리

        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.OK.value(), "로그아웃을 성공했습니다."));
    }

    private void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");          // 쿠키가 설정된 path와 동일하게
        cookie.setHttpOnly(true);     // 필요에 따라
        cookie.setMaxAge(0);          // 즉시 만료
        // cookie.setSecure(true);    // HTTPS 전용이면 활성화
        response.addCookie(cookie);
    }
}