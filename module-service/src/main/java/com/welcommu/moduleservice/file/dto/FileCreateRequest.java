package com.welcommu.moduleservice.file.dto;



import com.welcommu.moduledomain.file.File;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class FileCreateRequest {

    public static File toEntity(MultipartFile file, String fileUrl) {
        return File.builder()
                .fileName(file.getOriginalFilename())
                .fileUrl(fileUrl)
                .fileSize(file.getSize())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
