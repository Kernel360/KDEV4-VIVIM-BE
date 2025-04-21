package com.welcommu.moduleapi.approval;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduleservice.approval.approvalDecision.DecisionService;
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionCreateRequest;
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionModifyRequest;
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionResponse;
import com.welcommu.moduleservice.approval.approvalDecision.dto.DecisionSendResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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
public class DecisionController {

    private final DecisionService decisionService;

    @PostMapping("/approval/{approvalId}/decision")
    @Operation(summary = "승인응답 생성")
    public ResponseEntity<ApiResponse> createDecision(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
        @PathVariable Long approvalId,
        @Valid @RequestBody DecisionCreateRequest request
    ) {
        decisionService.createDecision(userDetails.getUser(), approvalId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponse(HttpStatus.CREATED.value(), "승인응답 생성을 성공했습니다."));
    }

    @PatchMapping("/decision/{decisionId}")
    @Operation(summary = "승인응답 수정")
    public ResponseEntity<ApiResponse> modifyDecision(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
        @PathVariable Long decisionId,
        @Valid @RequestBody DecisionModifyRequest request
    ) {
        decisionService.modifyDecision(decisionId, request);
        return ResponseEntity.ok().body(
            new ApiResponse(HttpStatus.OK.value(), "승인응답 수정을 성공했습니다.")
        );
    }

    @DeleteMapping("/decision/{decisionId}")
    @Operation(summary = "승인응답 삭제")
    public ResponseEntity<ApiResponse> deleteDecision(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
        @PathVariable Long decisionId
    ) {
        decisionService.deleteDecision(decisionId);
        return ResponseEntity.ok().body(
            new ApiResponse(HttpStatus.OK.value(), "승인응답 삭제를 성공했습니다.")
        );
    }

    @GetMapping("/decision/{decisionId}")
    @Operation(summary = "승인응답 단일조회")
    public ResponseEntity<DecisionResponse> getDecision(@PathVariable Long decisionId) {
        DecisionResponse response = decisionService.getDecision(decisionId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/approval/{approvalId}/decisions")
    @Operation(summary = "승인요청별 응답 전체조회")
    public ResponseEntity<List<DecisionResponse>> getDecisionsByProposal(
        @PathVariable Long approvalId
    ) {
        List<DecisionResponse> responses = decisionService.getAllDecision(approvalId);
        return ResponseEntity.ok().body(responses);
    }

    @PostMapping("/decision/{decisionId}/send")
    @Operation(summary = "승인응답 전송")
    public ResponseEntity<DecisionSendResponse> sendDecision(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
        @PathVariable Long decisionId
    ) {
        DecisionSendResponse response = decisionService.sendDecision(userDetails.getUser(),
            decisionId);
        return ResponseEntity.ok().body(response);
    }
}
