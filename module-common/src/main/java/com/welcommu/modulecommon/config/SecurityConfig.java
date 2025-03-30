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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(it -> {
                    it
                            .requestMatchers(
                                    PathRequest.toStaticResources().atCommonLocations()
                            ).permitAll()

<<<<<<< HEAD
                                    // Swagger 관련 URL 허용 (GET 요청)
                            .requestMatchers(HttpMethod.GET, SWAGGER).permitAll()
                            .requestMatchers("/api/login").permitAll()
//                            .anyRequest().authenticated();
                            .anyRequest().permitAll();
=======
                            .requestMatchers(HttpMethod.GET, SWAGGER).permitAll()

                            // Swagger 테스트 시 사용. 배포할 때 삭제
//                            .requestMatchers("/api/**").permitAll()

                            // Swagger 관련 URL 허용 (GET 요청)
                            .requestMatchers(HttpMethod.GET, SWAGGER).permitAll()

                            .requestMatchers("/api/login").permitAll() // 로그인 API는 인증 필요

                            // 다른 모든 요청은 인증 필요
                            .anyRequest().authenticated();
//                            .anyRequest().permitAll(); // 나머지 모든 요청도 인증 없이 허용
>>>>>>> 7a2cb0b ([feature] Company별 직원조회 추가 (#43))
                })
                .formLogin(Customizer.withDefaults());
        System.out.println("🔥 Security 설정 적용됨!");

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // React 앱의 주소
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {

        // hash로 암호화
        return new BCryptPasswordEncoder();
    }
}