package com.welcommu.moduleservice.file.dto;

import com.welcommu.moduledomain.file.File;
import com.welcommu.moduledomain.file.ReferenceType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileRequest {

    private String fileName;
    private String fileUrl;
    private Long fileSize;
    private String objectKey;

    public File toEntity(FileRequest request, ReferenceType referenceType, Long referenceId) {
        return File.builder()
            .fileName(request.fileName)
            .fileUrl(request.fileUrl)
            .fileSize(request.fileSize)
            .createdAt(LocalDateTime.now())
            .referenceType(referenceType)
            .referenceId(referenceId)
            .build();
    }
}
