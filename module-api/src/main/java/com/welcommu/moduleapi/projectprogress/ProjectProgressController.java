package com.welcommu.moduleapi.projectprogress;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.user.CustomUserDetails;
import com.welcommu.moduleservice.projectProgess.ProjectProgressService;
import com.welcommu.moduleservice.projectProgess.dto.ProgressCreateRequest;
import com.welcommu.moduleservice.projectProgess.dto.ProgressListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/projects")
@Tag(name = "프로젝트 단계 API", description = "프로젝트 단계를 셍성, 수정, 삭제, 이동시킬 수 있습니다.")
public class ProjectProgressController {

    private ProjectProgressService progressService;

    @PostMapping("/{projectId}/progress")
    @Operation(summary = "프로젝트 단계 생성")
    public ResponseEntity<ApiResponse> createProjectProgress(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long projectId,
        @RequestBody ProgressCreateRequest request
    ) {

        progressService.createProgress(userDetails.getUser(), projectId, request);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.CREATED.value(),"프로젝트 단계 생성을 성공했습니다."));
    }

    @PutMapping("/{projectId}/progress/{progressId}")
    @Operation(summary = "프로젝트 단계 수정")
    public ResponseEntity<ApiResponse> updateProgress(
        @PathVariable Long projectId,
        @PathVariable Long progressId,
        @RequestBody ProgressCreateRequest request
    ) {
        progressService.updateProgress(projectId, progressId, request);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "프로젝트 단계 수정 성공"));
    }

    @DeleteMapping("/{projectId}/progress/{progressId}")
    @Operation(summary = "프로젝트 단계 삭제")
    public ResponseEntity<ApiResponse> deleteProgress(
        @PathVariable Long projectId,
        @PathVariable Long progressId
    ) {
        progressService.deleteProgress(projectId, progressId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "프로젝트 단계 삭제 성공"));
    }

    @GetMapping("/{projectId}/progress")
    @Operation(summary = "프로젝트 단계 전체조회")
    public ResponseEntity<ProgressListResponse> getProgressList(
        @PathVariable Long projectId, @RequestBody ProgressListResponse request) {

        ProgressListResponse progressList = progressService.getProgressList(projectId);
        return ResponseEntity.ok(progressList);
    }
}