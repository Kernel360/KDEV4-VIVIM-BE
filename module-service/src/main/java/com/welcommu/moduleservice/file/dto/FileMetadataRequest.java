package com.welcommu.moduleservice.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileMetadataRequest {

    private String fileName;
    private Long fileSize;
    private String contentType;
}
