package com.welcommu.moduleapi.approval;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduleservice.approve.ApprovalService;
import com.welcommu.moduleservice.approve.dto.ApprovalCreateRequest;
import com.welcommu.moduleservice.approve.dto.ApprovalListResponse;
import com.welcommu.moduleservice.approve.dto.ApprovalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/checklists")
@Tag(name = "승인요청 API", description = "승인요청을 생성, 수정, 삭제시킬 수 있습니다.")
public class ApprovalController {

    private final ApprovalService approvalService;

    @PostMapping("/approve")
    @Operation(summary = "승인요청 생성")
    public ResponseEntity<ApiResponse> createApproval(
        @PathVariable Long checklistId,
        @RequestBody ApprovalCreateRequest request
    ) {
        approvalService.createApproval(checklistId, request);
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.CREATED.value(), "승인요청 생성을 성공했습니다."));
    }

    @GetMapping("/approve/{approvalId}")
    @Operation(summary = "승인요청 단일조회")
    public ResponseEntity<ApprovalResponse> getApproval(
        @PathVariable Long approvalId
    ) {
        ApprovalResponse response = approvalService.getApproval(approvalId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/approve")
    @Operation(summary = "승인요청 전체조회")
    public ResponseEntity<ApprovalListResponse> getApprovalList(
        @PathVariable Long checklistId
    ) {
        ApprovalListResponse response = approvalService.getApprovalList(checklistId);
        return ResponseEntity.ok(response);
    }
}