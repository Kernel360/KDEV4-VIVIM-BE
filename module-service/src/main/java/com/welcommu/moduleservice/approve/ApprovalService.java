package com.welcommu.moduleservice.approve;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.approval.Approval;
import com.welcommu.moduledomain.checklist.Checklist;
import com.welcommu.modulerepository.approval.ApprovalRepository;
import com.welcommu.modulerepository.checklist.ChecklistRepository;
import com.welcommu.moduleservice.approve.dto.ApprovalCreateRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApprovalService {

    private final ChecklistRepository checklistRepository;
    private final ApprovalRepository approvalRepository;

    public void createApproval(Long checklist_id, ApprovalCreateRequest request) {
        Checklist checklist = findChecklist(checklist_id);
        Approval approval = request.toEntity(checklist);
        approvalRepository.save(approval);
    }

    private Checklist findChecklist(Long checklist_id) {
        return checklistRepository.findById(checklist_id)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_CHECKLIST));
    }
}
