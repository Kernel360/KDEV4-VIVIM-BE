package com.welcommu.modulecommon.config;

import com.welcommu.modulecommon.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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

    @Autowired
    private JwtUtil jwtUtil;

    // application.properties에서 CORS 허용 도메인 목록을 주입
    @Value("${cors.allowedOrigins}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil);
        httpSecurity
                .cors(Customizer.withDefaults()) // CORS 설정 추가
                .csrf(csrf -> csrf.disable())  // CSRF 비활성화 (deprecated)
                .authorizeHttpRequests(it -> {
                    it
                            .requestMatchers(
                                    PathRequest.toStaticResources().atCommonLocations()
                            ).permitAll() // 정적 리소스 허용

                            // Swagger 테스트 시 사용. 배포할 때 삭제
                            .requestMatchers(HttpMethod.GET, SWAGGER).permitAll()

                            .requestMatchers("/api/login").permitAll() // 로그인 경로 허용

                            .anyRequest().authenticated(); // 나머지 경로는 인증 필요
                })
                .formLogin(Customizer.withDefaults()); // 기본 로그인 폼 사용

        // JWT 인증 필터 추가
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        System.out.println("🔥 Security 설정 적용됨!");

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 비밀번호 암호화 처리
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // application.properties에서 CORS 허용 도메인 목록 주입
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
