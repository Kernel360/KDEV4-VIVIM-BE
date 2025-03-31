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
                            ).permitAll(
                                
                            // Swagger 관련 URL 허용 (GET 요청)
                            .requestMatchers(HttpMethod.GET, SWAGGER).permitAll()
                            .requestMatchers("/api/login").permitAll()
                            .anyRequest().authenticated();
//                            .anyRequest().permitAll();
                })
                .formLogin(Customizer.withDefaults());
        System.out.println("🔥 Security 설정 적용됨!");

        return httpSecurity.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {

        // hash로 암호화
        return new BCryptPasswordEncoder();
    }
}
