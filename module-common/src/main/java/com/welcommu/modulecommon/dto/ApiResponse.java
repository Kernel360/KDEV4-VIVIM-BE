package com.welcommu.modulecommon.dto;

import lombok.Getter;

@Getter
public class ApiResponse {

    private int statusCode;
    private String statusMessage;

    public ApiResponse(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}
