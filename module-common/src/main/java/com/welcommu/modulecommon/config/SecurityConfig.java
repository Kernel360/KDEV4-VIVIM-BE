package com.welcommu.modulecommon.config;

import com.welcommu.modulecommon.token.helper.JwtTokenHelper;
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
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] SWAGGER = {
            "/swagger-ui/index.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    private final JwtTokenHelper jwtTokenHelper;

    @Value("${cors.allowedOrigins}")
    private String allowedOrigins;

    // Constructor-based injection of JwtTokenHelper
    public SecurityConfig(JwtTokenHelper jwtTokenHelper) {
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // JwtAuthenticationFilter에 JwtTokenHelper 주입
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenHelper);

        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
<<<<<<< Updated upstream
<<<<<<< Updated upstream
                .authorizeHttpRequests(it -> {it
                  .requestMatchers(
                      PathRequest.toStaticResources().atCommonLocations()
                  ).permitAll() // 정적 리소스 허용

                  .requestMatchers("/swagger-ui/**").permitAll()  // Swagger UI 페이지 접근 가능
                  .requestMatchers("/v3/api-docs/**").authenticated()  // API 문서는 인증 필요

                  .requestMatchers("/api/login").permitAll() // 로그인 API는 인증 없이 허용
=======
=======
>>>>>>> Stashed changes
                .authorizeHttpRequests(it -> it
                        .requestMatchers(
                                PathRequest.toStaticResources().atCommonLocations()
                        ).permitAll()
                        // Swagger 테스트 시 사용. 배포할 때 삭제
                        .requestMatchers(HttpMethod.GET, SWAGGER).permitAll()
                        .requestMatchers("/api/login").permitAll()
                        .anyRequest().authenticated()
                );
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes

                  .anyRequest().authenticated();                 
                  })
          
        // JWT 인증 필터 추가
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        System.out.println("🔥 Security 설정 적용됨!");

        return httpSecurity.build();
    }

<<<<<<< Updated upstream
<<<<<<< Updated upstream
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


=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
    @Bean
    public PasswordEncoder passwordEncoder() {
        // hash로 암호화
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
