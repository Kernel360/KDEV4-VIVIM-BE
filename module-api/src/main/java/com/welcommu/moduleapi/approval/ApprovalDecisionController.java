package com.welcommu.moduleapi.approval;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduleservice.approval.approvalDecision.DecisionService;
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionRequestCreation;
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionRequestModification;
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionResponsesByAllApprover;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "승인응답 API", description = "승인응답을 생성, 수정, 삭제, 전송시킬 수 있습니다.")
public class ApprovalDecisionController {

    private final DecisionService decisionService;

    @PostMapping("/approver/{approverId}/decision")
    @Operation(summary = "승인응답 생성")
    public ResponseEntity<ApiResponse> createDecision(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
        @PathVariable Long approverId,
        @Valid @RequestBody DecisionRequestCreation request) {

        Long decisionId = decisionService.createDecision(userDetails.getUser(), approverId,
            request);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponse(HttpStatus.CREATED.value(), "승인응답 생성을 성공했습니다.", decisionId));
    }

    @PatchMapping("/decision/{decisionId}")
    @Operation(summary = "승인응답 수정")
    public ResponseEntity<ApiResponse> modifyDecision(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails, @PathVariable Long decisionId,
        @Valid @RequestBody DecisionRequestModification request) {

        decisionService.modifyDecision(userDetails.getUser(), decisionId, request);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "승인응답 수정을 성공했습니다."));
    }

    @DeleteMapping("/decision/{decisionId}")
    @Operation(summary = "승인응답 삭제")
    public ResponseEntity<ApiResponse> deleteDecision(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails, @PathVariable Long decisionId) {

        decisionService.deleteDecision(userDetails.getUser(), decisionId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "승인응답 삭제를 성공했습니다."));
    }

    @GetMapping("/approval/{approvalId}/decision")
    @Operation(summary = "하나의 승인요청에 대한 모든 승인권자의 응답 전체조회")
    public ResponseEntity<DecisionResponsesByAllApprover> getFilteredAllDecision(
        @PathVariable Long approvalId) {

        DecisionResponsesByAllApprover response = (DecisionResponsesByAllApprover) decisionService.getFilteredAllDecisions(
            approvalId);
        return ResponseEntity.ok().body(response);
    }
}
