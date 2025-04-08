package com.welcommu.moduleapi.projectpost;

import static com.welcommu.modulecommon.util.IpUtil.getClientIp;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduleservice.projectpost.ProjectPostService;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostDetailResponse;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostListResponse;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/projects/{projectId}/posts")
@RequiredArgsConstructor
@Tag(name = "게시글 API", description = "게시글을 생성, 전체 조회, 상세 조회, 수정, 삭제, 이동시킬 수 있습니다.")
public class ProjectPostController {

    private final ProjectPostService projectPostService;

    @PostMapping
    @Operation(summary = "게시글 생성")
    public ResponseEntity<Long> createPost(@PathVariable Long projectId, @RequestBody ProjectPostRequest request, HttpServletRequest httpRequest) {
        String clientIp = getClientIp(httpRequest);
        Long postId =projectPostService.createPost(projectId, request, clientIp);
        return ResponseEntity.ok().body(postId);
    }

    @PutMapping("/{postId}")
    @Operation(summary = "게시글 수정")
    public ResponseEntity<ApiResponse> modifyPost(@PathVariable Long projectId, @PathVariable Long postId, @RequestBody ProjectPostRequest request) {
        projectPostService.modifyPost(projectId, postId, request);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "게시글이 수정되었습니다."));
    }

    @GetMapping
    @Operation(summary = "게시글 목록 조회")
    public ResponseEntity<List<ProjectPostListResponse>> getPostList(@PathVariable Long projectId) {
        List<ProjectPostListResponse> resultList = projectPostService.getPostList(projectId);
        return ResponseEntity.ok(resultList);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 상세 조회")
    public ResponseEntity<ProjectPostDetailResponse> getPostDetail(@PathVariable Long projectId, @PathVariable Long postId) {
        ProjectPostDetailResponse result = projectPostService.getPostDetail(projectId, postId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long projectId, @PathVariable Long postId) {
        projectPostService.deletePost(projectId, postId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "게시글이 삭제되었습니다."));
    }
}
