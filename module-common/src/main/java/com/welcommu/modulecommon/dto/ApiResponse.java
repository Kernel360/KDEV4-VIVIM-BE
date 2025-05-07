package com.welcommu.modulecommon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse {

    private int statusCode;
    private String statusMessage;
    private Object data;

    public ApiResponse(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.data = null;
    }
}