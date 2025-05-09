package com.welcommu.moduleservice.file.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompleteUploadRequest {

    String uploadId;
    String key;
    List<PartInfo> parts;
}