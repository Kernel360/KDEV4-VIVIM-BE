package com.welcommu.moduleapi.projectprogress;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduleservice.projectProgess.ProjectProgressService;
import com.welcommu.moduleservice.projectProgess.dto.ProgressApprovalStatusOverallResponse;
import com.welcommu.moduleservice.projectProgess.dto.ProgressApprovalStatusResponse;
import com.welcommu.moduleservice.projectProgess.dto.ProgressCreateRequest;
import com.welcommu.moduleservice.projectProgess.dto.ProgressListResponse;
import com.welcommu.moduleservice.projectProgess.dto.ProgressNameModifyRequest;
import com.welcommu.moduleservice.projectProgess.dto.ProgressPositionModifyRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/api/projects")
@AllArgsConstructor
@Tag(name = "프로젝트 단계 API", description = "프로젝트 단계를 셍성, 수정, 삭제, 이동시킬 수 있습니다.")
public class ProjectProgressController {

    private final ProjectProgressService progressService;

    @PostMapping("/{projectId}/progress")
    @Operation(summary = "프로젝트 단계 생성")
    public ResponseEntity<ApiResponse> createProjectProgress(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
        @PathVariable Long projectId,
        @RequestBody ProgressCreateRequest request
    ) {

        progressService.createProgress(userDetails.getUser(), projectId, request);
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.CREATED.value(), "프로젝트 단계 생성을 성공했습니다."));
    }

    @PutMapping("/{projectId}/progress/{progressId}/naming")
    @Operation(summary = "프로젝트 단계명 수정")
    public ResponseEntity<ApiResponse> modifyProgressName(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
        @PathVariable Long projectId,
        @PathVariable Long progressId,
        @RequestBody ProgressNameModifyRequest request
    ) {

        progressService.modifyProgressName(userDetails.getUser(), projectId, progressId, request);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "프로젝트 단계 수정 성공"));
    }

    @PutMapping("/{projectId}/progress/{progressId}/positioning")
    @Operation(summary = "프로젝트 단계 위치 수정")
    public ResponseEntity<ApiResponse> modifyProgressPosition(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
        @PathVariable Long projectId,
        @PathVariable Long progressId,
        @RequestBody ProgressPositionModifyRequest request
    ) {

        progressService.modifyProgressPosition(userDetails.getUser(), projectId, progressId, request);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "프로젝트 단계 수정 성공"));
    }

    @DeleteMapping("/{projectId}/progress/{progressId}")
    @Operation(summary = "프로젝트 단계 삭제")
    public ResponseEntity<ApiResponse> deleteProgress(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
        @PathVariable Long projectId,
        @PathVariable Long progressId
    ) {

        progressService.deleteProgress(userDetails.getUser(), projectId, progressId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "프로젝트 단계 삭제 성공"));
    }

    @GetMapping("/{projectId}/progress")
    @Operation(summary = "프로젝트 단계 전체조회")
    public ResponseEntity<ProgressListResponse> getProgressList(
        @PathVariable Long projectId) {

        ProgressListResponse progressList = progressService.getProgressList(projectId);
        return ResponseEntity.ok(progressList);
    }

    @GetMapping("/{projectId}/progress/status")
    @Operation(summary = "프로젝트 단계별 승인요청 진척도 조회")
    public ResponseEntity<ProgressApprovalStatusResponse> getProgressApprovalStatus(
        @PathVariable Long projectId
    ) {
        ProgressApprovalStatusResponse response = progressService.getProgressApprovalStatus(projectId);
        log.info("프로젝트 단계별 승인요청 진척도 조회 완료: projectId={}, count={}", projectId,
            response.getProgressList().size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{projectId}/progress/overall-progress")
    @Operation(summary = "프로젝트 전체 진행률 조회")
    public ResponseEntity<ProgressApprovalStatusOverallResponse> getOverallProgress(
        @PathVariable Long projectId
    ) {
        ProgressApprovalStatusOverallResponse response = progressService.calculateOverallProgress(projectId);
        log.info("프로젝트 전체 진행률 조회 완료: projectId={}, overallProgressRate={}", projectId,
            response.getOverallProgressRate());
        return ResponseEntity.ok(response);
    }
}