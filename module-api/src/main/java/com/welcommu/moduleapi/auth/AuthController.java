package com.welcommu.moduleapi.auth;

import com.welcommu.modulecommon.token.helper.JwtTokenHelper;
import com.welcommu.modulecommon.token.model.TokenDto;
import com.welcommu.moduleservice.user.UserService;
import com.welcommu.moduledomain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenHelper jwtTokenHelper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String email, @RequestParam String password) {
        try {
            User user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("Invalid email or password");
            }

            // 비밀번호가 일치하면 JWT 토큰 생성
            Map<String, Object> claims = new HashMap<>();
            claims.put("email", user.getEmail());

            TokenDto accessToken = jwtTokenHelper.issueAccessToken(claims);

            TokenDto refreshToken = jwtTokenHelper.issueRefreshToken(claims);

            // 토큰을 JSON 형식으로 반환
            Map<String, String> response = new HashMap<>();
            response.put("access_token", "Bearer " + accessToken.getToken());
            response.put("refresh_token", "Bearer " + refreshToken.getToken());

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Internal server error", "error", e.getMessage()));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<Map<String, String>> getUserInfo(@AuthenticationPrincipal String username) {
        Map<String, String> response = new HashMap<>();
        response.put("authenticatedUser", username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestParam String refreshToken) {
        try {
            Map<String, Object> claims = jwtTokenHelper.validationTokenWithThrow(refreshToken);

            TokenDto newAccessToken = jwtTokenHelper.issueAccessToken(claims);

            Map<String, String> response = new HashMap<>();
            response.put("access_token", "Bearer " + newAccessToken.getToken());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Internal server error", "error", e.getMessage()));
        }
    }
}
