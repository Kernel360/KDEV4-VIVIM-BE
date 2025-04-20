package com.welcommu.moduleapi.approval;

import com.welcommu.moduleservice.approval.approvalApprover.ApproverService;
import com.welcommu.moduleservice.approval.approvalApprover.dto.ApproverResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/approval")
@Tag(name = "승인권자 API", description = "승인권자를 생성, 수정, 삭제시킬 수 있습니다.")
public class ApproverController {

    private final ApproverService approverService;

    @GetMapping("/{approvalId}/approvers")
    public ResponseEntity<List<ApproverResponse>> getAllApprover(@PathVariable Long approvalId) {
        List<ApproverResponse> responses = approverService.getAllApprover(approvalId);
        return ResponseEntity.ok(responses);
    }
}
