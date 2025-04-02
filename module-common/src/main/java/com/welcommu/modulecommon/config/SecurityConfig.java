package com.welcommu.modulecommon.config;

import com.welcommu.modulecommon.token.helper.JwtTokenHelper;
import com.welcommu.modulecommon.token.model.TokenDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    private static final String[] SWAGGER = {
            "/swagger-ui/index.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    private final JwtTokenHelper jwtTokenHelper;

    @Value("${cors.allowedOrigins}")
    private String allowedOrigins;

    public SecurityConfig(JwtTokenHelper jwtTokenHelper) {
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenHelper);

        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(it -> it
                        .requestMatchers(
                                PathRequest.toStaticResources().atCommonLocations()
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, SWAGGER).permitAll()
                        .requestMatchers("/api/login").permitAll()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")  // 로그인 페이지 URL을 /login으로 설정
                        .permitAll()  // 로그인 페이지는 모두 접근 가능
                        .successHandler(new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                // 로그인 성공 시 JWT 토큰을 생성하여 응답에 추가
                                Map<String, Object> claims = new HashMap<>();
                                claims.put("username", authentication.getName());  // 예: 사용자 이름 추가
                                TokenDto accessToken = jwtTokenHelper.issueAccessToken(claims);  // JWT 발급

                                // JWT 토큰을 Authorization 헤더에 포함시켜 응답
                                response.setHeader("Authorization", "Bearer " + accessToken.getToken());
                                response.setStatus(HttpServletResponse.SC_OK);
                            }
                        })
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        log.info("🔥 Security 설정 적용됨!");
                .authorizeHttpRequests(it -> {
                    it
                            .requestMatchers(
                                    PathRequest.toStaticResources().atCommonLocations()
                            ).permitAll()

                            // Swagger 관련 URL 허용 (GET 요청)
                            .requestMatchers(HttpMethod.GET, SWAGGER).permitAll()
                            .requestMatchers("/api/login").permitAll()
//                            .anyRequest().authenticated();
                            .anyRequest().permitAll();
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
        return new BCryptPasswordEncoder();
    }
}
