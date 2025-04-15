package com.welcommu.moduleapi.approval;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduleservice.approve.ApprovalService;
import com.welcommu.moduleservice.approve.dto.ApprovalCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/checklists/{checklistId}")
@Tag(name = "승인요청 API", description = "승인요청을 셍성, 수정, 삭제시킬 수 있습니다.")
public class ApprovalController {

    private final ApprovalService approvalService;

    @PostMapping("/approve")
    @Operation(summary = "승인요청 생성")
    public ResponseEntity<ApiResponse> createApproval(
        @PathVariable Long checklistId,
        @RequestBody ApprovalCreateRequest request
    ) {
        approvalService.createApproval(checklistId, request);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.CREATED.value(),"승인요청 생성을 성공했습니다."));
    }
}
