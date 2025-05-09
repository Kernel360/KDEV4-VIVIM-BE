package com.welcommu.moduleservice.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresignedPart {

    private int partNumber;
    private String presignedUrl;
}