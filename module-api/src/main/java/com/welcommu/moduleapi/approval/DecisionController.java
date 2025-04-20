package com.welcommu.moduleapi.approval;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduleservice.approval.approvalDecision.DecisionService;
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionCreateRequest;
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionModifyRequest;
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "승인응답 API", description = "승인응답을 생성, 수정, 삭제, 전송시킬 수 있습니다.")
public class DecisionController {

    private final DecisionService decisionService;

    @PostMapping("/{approvalId}/decision")
    @Operation(summary = "승인응답 생성")
    public ResponseEntity<ApiResponse> createDecision(
            @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
            @PathVariable Long approvalId,
            @RequestBody DecisionCreateRequest request
    ) {
        decisionService.createDecision(userDetails.getUser(), approvalId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(), "승인 응답이 성공적으로 저장되었습니다."));
    }

    @PatchMapping("/decision/{decisionId}")
    @Operation(summary = "승인응답 수정")
    public ResponseEntity<ApiResponse> modifyDecision(
            @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
            @PathVariable Long decisionId,
            @RequestBody DecisionModifyRequest request
    ) {
        decisionService.modifyDecision(userDetails.getUser(), decisionId, request);
        return ResponseEntity.ok(
                new ApiResponse(HttpStatus.OK.value(), "승인 응답이 성공적으로 수정되었습니다.")
        );
    }

    @DeleteMapping("/decision/{decisionId}")
    @Operation(summary = "승인응답 삭제")
    public ResponseEntity<ApiResponse> deleteDecision(
            @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
            @PathVariable Long decisionId
    ) {
        decisionService.deleteDecision(userDetails.getUser(), decisionId);
        return ResponseEntity.ok(
                new ApiResponse(HttpStatus.OK.value(), "승인 응답이 삭제되었습니다.")
        );
    }

    @GetMapping("/{approvalId}/decisions")
    @Operation(summary = "승인요청별 응답 전체 조회")
    public ResponseEntity<List<DecisionResponse>> getDecisionsByProposal(
            @PathVariable Long approvalId
    ) {
        List<DecisionResponse> responses = decisionService.getAllDecisionsByApproval(approvalId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/decision/{decisionId}")
    @Operation(summary = "승인응답 단건 조회")
    public ResponseEntity<DecisionResponse> getDecision(
            @PathVariable Long decisionId
    ) {
        DecisionResponse response = decisionService.getDecision(decisionId);
        return ResponseEntity.ok(response);
    }
}
