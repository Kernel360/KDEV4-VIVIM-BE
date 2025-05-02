package com.welcommu.moduleservice.file.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MultipartPresignedUrlResponse {

    private String objectKey;
    private String uploadId;
    private String fileUrl;
    private List<PresignedPart> presignedParts;
}
