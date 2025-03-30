package com.welcommu.modulecommon.token.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {

    private String token; // JWT 토큰
    private LocalDateTime expiredAt; // 토큰 만료 시간

}