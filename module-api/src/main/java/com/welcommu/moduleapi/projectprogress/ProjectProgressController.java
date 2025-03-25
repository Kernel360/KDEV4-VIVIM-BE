package com.welcommu.moduleapi.projectprogress;

import com.welcommu.dto.ApiResponse;
import com.welcommu.moduledomain.projectProgess.ProjectProgressService;
import com.welcommu.moduledomain.projectProgess.dto.ProjectProgressRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        @PathVariable Long projectId,
        @RequestBody ProjectProgressRequest request
    ) {

        progressService.create(projectId, request);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.CREATED.value(),"프로젝트 단계 생성을 성공했습니다."));
    }

}
