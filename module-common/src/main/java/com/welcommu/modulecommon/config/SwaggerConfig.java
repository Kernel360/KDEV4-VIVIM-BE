package com.welcommu.modulecommon.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SwaggerConfig implements WebMvcConfigurer {

    // API 그룹을 정의하여 Swagger UI에서 API를 관리
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public") // 그룹 이름
                .pathsToMatch("/api/**") // API 경로
                .addOperationCustomizer((operation, handlerMethod) -> operation.addSecurityItem(
                        new SecurityRequirement().addList("Bearer"))) // JWT 인증을 요구하는 모든 API에 적용
                .build();
    }

    // Springdoc에서 JWT Bearer 토큰을 설정하는 부분
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("API Documentation")
                        .description("This is the API documentation for the JWT-based authentication system.")
                        .version("1.0"))
                .components(new Components().addSecuritySchemes("Bearer",
                        new SecurityScheme().type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER).name("Authorization"))) // API Key 설정
                .addSecurityItem(new SecurityRequirement().addList("Bearer")); // 모든 API에 Bearer 보안 적용
    }
}
