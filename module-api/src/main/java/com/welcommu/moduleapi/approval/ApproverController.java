package com.welcommu.moduleapi.approval;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduleservice.approval.approvalApprover.ApproverService;
import com.welcommu.moduleservice.approval.approvalApprover.dto.ApproverRegisterRequest;
import com.welcommu.moduleservice.approval.approvalApprover.dto.ApproverResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
public class ApproverController {

    private final ApproverService approverService;

    @PostMapping("/{approvalId}/approvers")
    @Operation(summary = "승인요청별 승인권자 등록")
    public ResponseEntity<ApiResponse> registerApprovers(
        @PathVariable Long approvalId,
        @RequestBody ApproverRegisterRequest request) {

        approverService.registerApprovers(approvalId, request);
        return ResponseEntity.ok().body(new ApiResponse(200, "승인권자 등록을 성공했습니다."));
    }

    @PutMapping("/{approvalId}/approvers")
    @Operation(summary = "승인권자 수정")
    public ResponseEntity<ApiResponse> modifyApprovers(
        @PathVariable Long approvalId,
        @RequestBody ApproverRegisterRequest request) {

        approverService.modifyApprovers(approvalId, request);
        return ResponseEntity.ok().body(new ApiResponse(200, "승인권자 수정을 성공했습니다."));
    }

    @DeleteMapping("/{approvalId}/approvers")
    @Operation(summary = "승인권자 전체삭제")
    public ResponseEntity<ApiResponse> deleteApprovers(@PathVariable Long approvalId) {
        approverService.deleteAllApprovers(approvalId);
        return ResponseEntity.ok().body(new ApiResponse(200, "승인권자 전체 삭제를 성공했습니다."));
    }

    @GetMapping("/{approvalId}/approvers")
    @Operation(summary = "승인권자 전체조회")
    public ResponseEntity<List<ApproverResponse>> getAllApprover(@PathVariable Long approvalId) {
        List<ApproverResponse> responses = approverService.getAllApprover(approvalId);
        return ResponseEntity.ok().body(responses);
    }
}
