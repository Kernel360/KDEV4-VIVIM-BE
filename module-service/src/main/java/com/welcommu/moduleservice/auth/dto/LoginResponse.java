package com.welcommu.moduleservice.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoginResponse {
    private String accessToken;
    private String refreshToken;

}
