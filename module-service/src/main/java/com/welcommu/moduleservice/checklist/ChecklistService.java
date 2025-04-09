package com.welcommu.moduleservice.checklist;


import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.checklist.Checklist;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.modulerepository.checklist.ChecklistRepository;
import com.welcommu.modulerepository.projectprogress.ProjectProgressRepository;
import com.welcommu.moduleservice.checklist.dto.ChecklistCreateRequest;
import com.welcommu.moduleservice.checklist.dto.ChecklistListResponse;
import com.welcommu.moduleservice.checklist.dto.ChecklistResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final ProjectProgressRepository progressRepository;

    public void createChecklist(Long progressId, ChecklistCreateRequest request) {

        ProjectProgress progress = progressRepository.findById(progressId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_CHECKLIST));

        Checklist checklist = request.toEntity(progress, request.getName());

        ChecklistResponse.of(checklistRepository.save(checklist));
    }

    public ChecklistListResponse getAllChecklist(Long progressId) {
        findProgress(progressId);
        List<Checklist> checkList = checklistRepository.findByProjectProgressId(progressId);

        return ChecklistListResponse.of(checkList);
    }

    public void modifyChecklist(Long checklistId, String name) {
        Checklist checklist = findChecklist(checklistId);

        if (checklist.getDeletedAt() != null) {
            throw new CustomException(CustomErrorCode.ALREADY_DELETED_CHECKLIST);
        }

        checklist.setName(name);
        checklistRepository.save(checklist);
    }

    public void deleteChecklist(Long checklistId) {
        Checklist checklist = findChecklist(checklistId);

        if (checklist.getDeletedAt() != null) {
            throw new CustomException(CustomErrorCode.ALREADY_DELETED_CHECKLIST);
        }

        checklist.markAsDeleted();
        checklistRepository.delete(checklist);
    }
    private void findProgress(Long progressId) {
        progressRepository.findById(progressId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROGRESS));
    }

    private Checklist findChecklist(Long checklistId) {
        return checklistRepository.findById(checklistId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_CHECKLIST));
    }
}
