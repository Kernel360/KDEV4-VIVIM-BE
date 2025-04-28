package com.welcommu.moduleservice.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PreSignedUrlResponse {

    private String preSignedUrl;
    private String fileUrl;
    private String objectKey;
}
