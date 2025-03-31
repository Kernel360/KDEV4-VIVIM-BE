package com.welcommu.moduleservice.file.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FileDownloadDto {

    private final String originalFileName; // ex: "report.pdf"
    private final byte[] fileData;         // byte 배열 형태의 파일 내용
    private final String contentType;      // ex: "application/pdf" 또는 "image/png"

    public String getOriginalFileName() {
        return originalFileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public String getContentType() {
        return contentType;
    }
}
