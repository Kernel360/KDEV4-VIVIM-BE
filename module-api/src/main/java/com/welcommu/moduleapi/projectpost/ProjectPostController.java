package com.welcommu.moduleapi.projectpost;

import static com.welcommu.modulecommon.util.IpUtil.getClientIp;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduleservice.projectpost.ProjectPostService;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostDetailResponse;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostListResponse;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostRequest;
import com.welcommu.moduleservice.projectpost.dto.RecentUserPostListRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Tag(name = "게시글 API", description = "게시글을 생성, 전체 조회, 상세 조회, 수정, 삭제, 이동시킬 수 있습니다.")
public class ProjectPostController {

    private final ProjectPostService projectPostService;


    @PostMapping("/api/projects/{projectId}/posts")
    @Operation(summary = "게시글 생성")
    public ResponseEntity<Long> createPost(@PathVariable Long projectId,
        @RequestBody ProjectPostRequest request, HttpServletRequest httpRequest,
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails) {
        String clientIp = getClientIp(httpRequest);
        Long actorId = userDetails.getUser().getId();
        Long postId = projectPostService.createPost(userDetails.getUser(), projectId, request,
            clientIp, actorId);
        return ResponseEntity.ok().body(postId);
    }

    @PutMapping("/api/projects/{projectId}/posts/{postId}")
    @Operation(summary = "게시글 수정")
    public ResponseEntity<ApiResponse> modifyPost(@PathVariable Long projectId,
        @PathVariable Long postId, @RequestBody ProjectPostRequest request,
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails) {
        Long actorId = userDetails.getUser().getId();
        projectPostService.modifyPost(projectId, postId, request, actorId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "게시글이 수정되었습니다."));
    }

    @GetMapping("/api/projects/{projectId}/posts")
    @Operation(summary = "게시글 목록 조회")
    public ResponseEntity<List<ProjectPostListResponse>> getPostList(@PathVariable Long projectId) {
        List<ProjectPostListResponse> resultList = projectPostService.getPostList(projectId);
        return ResponseEntity.ok(resultList);
    }

    @GetMapping("/api/posts/admin/recent")
    @Operation(summary = "최근 게시글 5개 조회")
    public ResponseEntity<List<ProjectPostListResponse>> getRecentAdminPostList() {
        List<ProjectPostListResponse> resultList = projectPostService.getRecentPostList();
        return ResponseEntity.ok(resultList);
    }

    @GetMapping("/api/posts/user/recent")
    @Operation(summary = "최근 게시글 5개 조회")
    public ResponseEntity<List<ProjectPostListResponse>> getRecentUserPostList(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails) {

        RecentUserPostListRequest request = new RecentUserPostListRequest(userDetails.getUser());

        List<ProjectPostListResponse> resultList = projectPostService.getRecentUserPostList(
            request);

        return ResponseEntity.ok(resultList);
    }

    @GetMapping("/api/projects/{projectId}/posts/{postId}")
    @Operation(summary = "게시글 상세 조회")
    public ResponseEntity<ProjectPostDetailResponse> getPostDetail(@PathVariable Long projectId,
        @PathVariable Long postId) {
        ProjectPostDetailResponse result = projectPostService.getPostDetail(projectId, postId);
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/api/projects/{projectId}/posts/{postId}")
    @Operation(summary = "게시글 삭제")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long projectId,
        @PathVariable Long postId, @AuthenticationPrincipal AuthUserDetailsImpl userDetails) {
        Long actorId = userDetails.getUser().getId();
        projectPostService.deletePost(projectId, postId, actorId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "게시글이 삭제되었습니다."));
    }

    @PatchMapping("/api/projects/{projectId}/posts/{postId}/answer")
    @Operation(summary = "질문 게시글 답변")
    public ResponseEntity<ApiResponse> answerPost(@PathVariable Long postId,
        @RequestBody String answer
    ) {
        projectPostService.completeAnswer(postId, answer);
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.OK.value(), "질문에 대한 답변이 등록되었습니다."));
    }
}
