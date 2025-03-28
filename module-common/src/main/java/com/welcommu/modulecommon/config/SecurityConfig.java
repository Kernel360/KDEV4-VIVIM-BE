package com.welcommu.modulecommon.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity // security 활성화
public class SecurityConfig {

    private static final String[] SWAGGER = {
            "/swagger-ui/index.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults()) // CORS 설정 추가
                .csrf(csrf -> csrf.disable())  // CSRF 비활성화 (deprecated)
                .authorizeHttpRequests(it -> {
                    it
                            .requestMatchers(
                                    PathRequest.toStaticResources().atCommonLocations()
                            ).permitAll() // 정적 리소스 허용

                            .requestMatchers(HttpMethod.GET, SWAGGER).permitAll()

                            // Swagger 테스트 시 사용. 배포할 때 삭제
//                            .requestMatchers("/api/**").permitAll()

                            .requestMatchers("/api/login").permitAll() // 로그인 API는 인증 필요

                            // 다른 모든 요청은 인증 필요
                            .anyRequest().authenticated();
//                            .anyRequest().permitAll(); // 나머지 모든 요청도 인증 없이 허용
                })
                .formLogin(Customizer.withDefaults()); // 기본 로그인 폼 사용
        System.out.println("🔥 Security 설정 적용됨!");

        return httpSecurity.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {

        // hash로 암호화
        return new BCryptPasswordEncoder();
    }
}