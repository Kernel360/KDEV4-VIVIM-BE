package com.welcommu.moduleservice.approve;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.approval.Approval;
import com.welcommu.moduledomain.checklist.Checklist;
import com.welcommu.modulerepository.approval.ApprovalRepository;
import com.welcommu.modulerepository.checklist.ChecklistRepository;
import com.welcommu.moduleservice.approve.dto.ApprovalCreateRequest;
import com.welcommu.moduleservice.approve.dto.ApprovalListResponse;
import com.welcommu.moduleservice.approve.dto.ApprovalResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApprovalService {

    private final ChecklistRepository checklistRepository;
    private final ApprovalRepository approvalRepository;

    public void createApproval(Long checklistId, ApprovalCreateRequest request) {
        Checklist checklist = findChecklist(checklistId);
        Approval approval = request.toEntity(checklist);
        approvalRepository.save(approval);
    }

    public ApprovalResponse getApproval(Long approvalId) {
        Approval approval = findApproval(approvalId);
        return ApprovalResponse.of(approval);
    }

    public ApprovalListResponse getApprovalList(Long checklistId) {
        Checklist checklist = findChecklist(checklistId);
        List<Approval> approvalList = approvalRepository.findByChecklist(checklist);
        return ApprovalListResponse.from(approvalList);
    }

    private Checklist findChecklist(Long checklistId) {
        return checklistRepository.findById(checklistId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_CHECKLIST));
    }

    private Approval findApproval(Long approvalId) {
        return approvalRepository.findById(approvalId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL));
    }
}
