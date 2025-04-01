package com.welcommu.modulecommon.controller;

import com.welcommu.modulecommon.security.JwtUtil;
import com.welcommu.moduleservice.user.UserService; // UserService import
import com.welcommu.moduledomain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(JwtUtil jwtUtil, UserService userService, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // 루트 경로에 대한 처리를 추가: / 경로로 접근 시 "Welcome" 메시지 출력
    @GetMapping("/")
    public String home() {
        return "Welcome to the Login API! Please use /api/login to authenticate.";
    }

    // 로그인 처리: /api/login 경로로 POST 요청을 처리
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String email, @RequestParam String password) {
        try {
            // 이메일로 사용자 조회
            User user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Invalid credentials"));

            // 비밀번호 확인
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
            }

            // 비밀번호가 일치하면 JWT 토큰 생성
            String token = jwtUtil.generateToken(user.getEmail()); // JWT 토큰 생성

            // 토큰을 JSON 형식으로 반환
            Map<String, String> response = new HashMap<>();
            response.put("token", "Bearer " + token);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Internal server error", "error", e.getMessage()));
        }
    }
}
