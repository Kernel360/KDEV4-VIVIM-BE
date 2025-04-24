package com.welcommu.moduleapi.approval;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduleservice.approval.approvalApprover.ApproverService;
import com.welcommu.moduleservice.approval.approvalApprover.dto.ApproverRequestCreate;
import com.welcommu.moduleservice.approval.approvalApprover.dto.ApproverResponseList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/approval")
@Tag(name = "승인권자 API", description = "승인권자를 생성, 수정, 삭제시킬 수 있습니다.")
public class ApprovalApproverController {

    private final ApproverService approverService;

    @PostMapping("/{proposalId}/approvers")
    @Operation(summary = "승인요청별 승인권자 등록")
    public ResponseEntity<ApiResponse> createApprover(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails, @PathVariable Long proposalId,
        @RequestBody ApproverRequestCreate request) {

        approverService.createApprover(userDetails.getUser(), proposalId, request);
        return ResponseEntity.ok().body(new ApiResponse(200, "승인권자 등록을 성공했습니다."));
    }

    @PutMapping("/{proposalId}/approvers")
    @Operation(summary = "승인권자 수정")
    public ResponseEntity<ApiResponse> modifyApprovers(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails, @PathVariable Long proposalId,
        @RequestBody ApproverRequestCreate request) {

        approverService.modifyApprovers(userDetails.getUser(), proposalId, request);
        return ResponseEntity.ok().body(new ApiResponse(200, "승인권자 수정을 성공했습니다."));
    }

    @DeleteMapping("/{proposalId}/approvers")
    @Operation(summary = "승인권자 전체삭제")
    public ResponseEntity<ApiResponse> deleteApprovers(@PathVariable Long proposalId) {
        approverService.deleteAllApprovers(proposalId);
        return ResponseEntity.ok().body(new ApiResponse(200, "승인권자 전체 삭제를 성공했습니다."));
    }

    // 승인요청별 승인권자 목록 조회
    @GetMapping("/{proposalId}/approvers")
    @Operation(summary = "승인요청별 승인권자 전체조회")
    public ResponseEntity<ApproverResponseList> getAllApprovers(@PathVariable Long proposalId) {

        ApproverResponseList response = approverService.getAllApprovers(proposalId);
        return ResponseEntity.ok().body(response);
    }
}
