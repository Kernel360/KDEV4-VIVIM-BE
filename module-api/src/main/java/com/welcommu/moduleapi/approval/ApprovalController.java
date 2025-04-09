package com.welcommu.moduleapi.approval;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduleservice.approval.ApprovalService;
import com.welcommu.moduleservice.approval.dto.ApprovalRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checklist")
@AllArgsConstructor
@Tag(name = "체크리스트 승인요청 API", description = "체크리스트에 대해 승인요청을 생성, 수정, 읽음처리시킬 수 있습니다.")
public class ApprovalController {

    private final ApprovalService approvalService;

    @PostMapping("/{checklistId}/approval")
    @Operation(summary = "체크리스트 승인요청 생성")
    public ResponseEntity<ApiResponse> createDevApproval(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
        @PathVariable Long checklistId,
        @RequestBody ApprovalRequest request
    ) {

        approvalService.createApproval(userDetails.getUser(), checklistId, request);
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.CREATED.value(), "체크리스트 승인요청 생성을 성공했습니다."));
    }
}
