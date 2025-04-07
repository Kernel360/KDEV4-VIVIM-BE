package com.welcommu.moduleservice.checklist.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChecklistCreateRequest {

    @NotBlank
    private String name;
}