package com.welcommu.moduleservice.file.dto;


import com.welcommu.moduledomain.file.File;
import com.welcommu.moduledomain.file.ReferenceType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MultipartFileMetadataRequest {

    private String fileName;
    private String contentType;
    private Long fileSize;

    public File toEntity(MultipartFileMetadataRequest request, ReferenceType referenceType,
        Long referenceId, String fileUrl) {
        return File.builder()
            .fileName(request.fileName)
            .fileUrl(fileUrl)
            .fileSize(request.fileSize)
            .createdAt(LocalDateTime.now())
            .referenceType(referenceType)
            .referenceId(referenceId)
            .build();
    }

}
