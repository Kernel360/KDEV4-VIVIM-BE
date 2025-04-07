package com.welcommu.moduleapi.checklist;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduleservice.checklist.ChecklistService;
import com.welcommu.moduleservice.checklist.dto.ChecklistCreateRequest;
import com.welcommu.moduleservice.checklist.dto.ChecklistListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/progress")
@AllArgsConstructor
@Tag(name = "체크리스트 API", description = "체크리스트를 생성, 수정, 삭제시킬 수 있습니다.")
public class ChecklistController {

    private ChecklistService checklistService;

    @PostMapping("/{progressId}/checklists")
    @Operation(summary = "체크리스트 생성")
    public ResponseEntity<ApiResponse> createChecklist(@PathVariable Long progressId, @RequestBody ChecklistCreateRequest request) {
        checklistService.createChecklist(progressId, request);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.CREATED.value(), "체크리스트가 생성되었습니다."));
    }

    @PutMapping("/{checklistId}")
    @Operation(summary = "체크리스트 수정")
    public ResponseEntity<ApiResponse> modifyChecklist(
        @PathVariable Long checklistId,
        @RequestParam String name
    ) {
        checklistService.modifyChecklist(checklistId, name);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "체크리스트가 수정되었습니다."));
    }

    @DeleteMapping("/{checklistId}")
    @Operation(summary = "체크리스트 삭제")
    public ResponseEntity<ApiResponse> deleteChecklist(@PathVariable Long checklistId) {
        checklistService.deleteChecklist(checklistId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "체크리스트가 삭제되었습니다."));
    }

    @GetMapping("/{progressId}/checklists")
    @Operation(summary = "체크리스트 전체조회")
    public ResponseEntity<ChecklistListResponse> getAllChecklist(@PathVariable Long progressId) {
        ChecklistListResponse checkListResponse = checklistService.getAllChecklist(progressId);
        return ResponseEntity.ok(checkListResponse);
    }
}