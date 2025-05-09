package com.welcommu.moduleapi.approval;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduleservice.approval.approvalProposal.ProposalService;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalCreateRequest;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalModifyRequest;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalResponse;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalResponseList;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalSendResponse;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/api")
@Tag(name = "승인요청 API", description = "승인요청을 생성, 수정, 삭제, 전송시킬 수 있습니다.")
public class ApprovalProposalController {

    private final ProposalService proposalService;

    @PostMapping("/progress/{progressId}/approval")
    @Operation(summary = "승인요청 생성")
    public ResponseEntity<ApiResponse> createApproval(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails, @PathVariable Long progressId,
        @Valid @RequestBody ProposalCreateRequest request) {

        Long approvalProposalId = proposalService.createProposal(userDetails.getUser(), progressId,
            request);
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.CREATED.value(), "승인요청 생성을 성공했습니다.",
                approvalProposalId));
    }

    @PatchMapping("/approval/{approvalId}")
    @Operation(summary = "승인요청 수정")
    public ResponseEntity<ApiResponse> modifyApproval(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails, @PathVariable Long approvalId,
        @RequestBody ProposalModifyRequest request) {

        proposalService.modifyProposal(userDetails.getUser(), approvalId, request);
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.CREATED.value(), "승인요청 수정을 성공했습니다."));
    }

    @DeleteMapping("/approval/{approvalId}")
    @Operation(summary = "승인요청 삭제")
    public ResponseEntity<ApiResponse> deleteApproval(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails, @PathVariable Long approvalId) {

        proposalService.deleteProposal(userDetails.getUser(), approvalId);
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.OK.value(), "승인요청 삭제를 성공했습니다."));
    }

    @PostMapping("/approval/{approvalId}/send")
    @Operation(summary = "승인요청 전송 : 작업 완료 후, 승인요청 고객사에 보내기")
    public ResponseEntity<ProposalSendResponse> sendApproval(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails, @PathVariable Long approvalId) {

        ProposalSendResponse response = proposalService.sendProposal(userDetails.getUser(),
            approvalId);
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping("/approval/{approvalId}")
    @Operation(summary = "승인요청 단일조회")
    public ResponseEntity<ProposalResponse> getApproval(@PathVariable Long approvalId) {

        ProposalResponse response = proposalService.getProposal(approvalId);
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping("/progress/{progressId}/approval")
    @Operation(summary = "승인요청 전체조회")
    public ResponseEntity<ProposalResponseList> getApprovalList(@PathVariable Long progressId) {

        ProposalResponseList response = proposalService.getAllProposal(progressId);
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping("/proposals/recent")
    @Operation(summary = "최근 작성된 승인요청 5개 조회")
    public ResponseEntity<ProposalResponseList> getRecentProposals() {
        ProposalResponseList response = proposalService.getRecentProposals();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/approval/{approvalId}/status")
    @Operation(summary = "최종 승인상태 확인")
    public ResponseEntity<ProposalStatusResponse> getProposalStatusSummary(
        @PathVariable Long approvalId) {
        ProposalStatusResponse response = proposalService.getProposalStatus(approvalId);
        return ResponseEntity.ok(response);
    }
}