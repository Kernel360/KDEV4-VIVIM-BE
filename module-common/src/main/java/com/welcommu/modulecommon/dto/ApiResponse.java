package com.welcommu.modulecommon.dto;

import lombok.Getter;

@Getter

public class ApiResponse {

    private int statusCode;
    private String statusMessage;
    private Object data;


    public ApiResponse(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public ApiResponse(int statusCode, String statusMessage, Object data) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.data=data;
    }
}
