package com.welcommu.moduleapi.approval;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduleservice.approval.approvalProposal.ProposalService;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalCreateRequest;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalModifyRequest;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalResponse;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalResponseList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/progress")
@Tag(name = "승인요청 API", description = "승인요청을 생성, 수정, 삭제시킬 수 있습니다.")
public class ApprovalProposalController {

    private final ProposalService proposalService;

    @PostMapping("/{progressId}/approval")
    @Operation(summary = "승인요청 생성")
    public ResponseEntity<ApiResponse> createApproval(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
        @PathVariable Long progressId,
        @RequestBody ProposalCreateRequest request) {

        proposalService.createApproval(userDetails.getUser(), progressId, request);
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.CREATED.value(), "승인요청 생성을 성공했습니다."));
    }

    @PatchMapping("/{progressId}/approval/{approvalId}")
    @Operation(summary = "승인요청 수정")
    public ResponseEntity<ApiResponse> modifyApproval(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
        @PathVariable Long progressId,
        @PathVariable Long approvalId,
        @RequestBody ProposalModifyRequest request) {

        proposalService.modifyApproval(userDetails.getUser(), progressId, approvalId,
            request);
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.CREATED.value(), "승인요청 수정을 성공했습니다."));
    }

    @DeleteMapping("/approval/{approvalId}")
    @Operation(summary = "승인요청 삭제")
    public ResponseEntity<ApiResponse> deleteApproval(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails, @PathVariable Long approvalId) {

        proposalService.deleteApproval(userDetails.getUser(), approvalId);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "승인요청 삭제를 성공했습니다."));
    }

    @GetMapping("/approval/{approvalId}")
    @Operation(summary = "승인요청 단일조회")
    public ResponseEntity<ProposalResponse> getApproval(@PathVariable Long approvalId) {

        ProposalResponse response = proposalService.getApproval(approvalId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{progressId}/approval")
    @Operation(summary = "승인요청 전체조회")
    public ResponseEntity<ProposalResponseList> getApprovalList(@PathVariable Long progressId) {

        ProposalResponseList response = proposalService.getApprovalList(progressId);
        return ResponseEntity.ok(response);
    }
}