package com.welcommu.moduleservice.file.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class FileDownloadUrlResponse {

    private String preSignedUrl;
    private String fileName;
    private Long fileSize;


}
