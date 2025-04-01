package com.welcommu.moduleapi.auth;

import com.welcommu.modulecommon.token.helper.JwtTokenHelper;
import com.welcommu.modulecommon.token.model.TokenDto;
import com.welcommu.moduleservice.user.UserService;
import com.welcommu.moduledomain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final JwtTokenHelper jwtTokenHelper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(JwtTokenHelper jwtTokenHelper, UserService userService, PasswordEncoder passwordEncoder) {
        this.jwtTokenHelper = jwtTokenHelper;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String email, @RequestParam String password) {
        try {
            // 이메일로 사용자 조회
            User user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

            // 비밀번호 확인
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("Invalid email or password");
            }

            // 비밀번호가 일치하면 JWT 토큰 생성
            Map<String, Object> claims = new HashMap<>();
            claims.put("email", user.getEmail());  // 클레임에 이메일 정보 추가

            // Access Token 발급
            TokenDto accessToken = jwtTokenHelper.issueAccessToken(claims); // JWT 토큰 생성
            // Refresh Token 발급
            TokenDto refreshToken = jwtTokenHelper.issueRefreshToken(claims); // Refresh Token 생성

            // 토큰을 JSON 형식으로 반환
            Map<String, String> response = new HashMap<>();
            response.put("access_token", "Bearer " + accessToken.getToken());
            response.put("refresh_token", "Bearer " + refreshToken.getToken()); // Refresh Token도 응답에 포함

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Internal server error", "error", e.getMessage()));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<Map<String, String>> getUserInfo(@AuthenticationPrincipal String username) {
        // 사용자의 이메일 정보 또는 username을 통해 사용자 정보 반환
        Map<String, String> response = new HashMap<>();
        response.put("authenticatedUser", username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestParam String refreshToken) {
        try {
            // Refresh Token 검증
            Map<String, Object> claims = jwtTokenHelper.validationTokenWithThrow(refreshToken);  // 유효성 검사

            // 새로운 Access Token 발급
            TokenDto newAccessToken = jwtTokenHelper.issueAccessToken(claims);

            // 새로운 Access Token 반환
            Map<String, String> response = new HashMap<>();
            response.put("access_token", "Bearer " + newAccessToken.getToken());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Internal server error", "error", e.getMessage()));
        }
    }
}
