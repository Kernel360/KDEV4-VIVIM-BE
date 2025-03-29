package com.welcommu.modulecommon.controller;

import com.welcommu.modulecommon.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    // 루트 경로에 대한 처리를 추가: / 경로로 접근 시 "Welcome" 메시지 출력
    @GetMapping("/")
    public String home() {
        return "Welcome to the Login API! Please use /api/login to authenticate.";
    }

    // 로그인 처리: /api/login 경로로 POST 요청을 처리
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String username, @RequestParam String password) {
        System.out.println("Login 메소드 호출됨");  // 로그 추가

        try {
            if ("testuser".equals(username) && "testpassword".equals(password)) {
                String token = jwtUtil.generateToken(username); // JWT 토큰 생성
                System.out.println("생성된 JWT 토큰: " + token); // 로그에 생성된 토큰 출력
                Map<String, String> response = new HashMap<>();
                response.put("token", "Bearer " + token); // 토큰을 JSON 형식으로 반환
                return ResponseEntity.ok(response); // 클라이언트에 토큰 반환
            } else {
                return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외 출력
            return ResponseEntity.status(500).body(Map.of("message", "Internal server error", "error", e.getMessage()));
        }
    }
}
