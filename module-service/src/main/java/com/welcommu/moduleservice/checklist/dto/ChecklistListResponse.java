package com.welcommu.moduleservice.checklist.dto;

import com.welcommu.moduledomain.checklist.Checklist;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChecklistListResponse {

    private List<ChecklistResponse> allChecklist;

    public static ChecklistListResponse of(List<Checklist> allChecklist){

        List<ChecklistResponse> checkListResponse = allChecklist.stream()
            .map(ChecklistResponse::of)
            .toList();

        return ChecklistListResponse.builder()
            .allChecklist(checkListResponse)
            .build();
    }
}
