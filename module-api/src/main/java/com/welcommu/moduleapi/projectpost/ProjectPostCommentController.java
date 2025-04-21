package com.welcommu.moduleapi.projectpost;

import static com.welcommu.modulecommon.util.IpUtil.getClientIp;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduleservice.projectpost.ProjectPostCommentService;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostCommentListResponse;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostCommentRequest;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
@Tag(name = "게시글 댓글 API", description = "댓글을 생성, 전체 조회, 수정, 삭제, 이동시킬 수 있습니다.")
public class ProjectPostCommentController {

    private final ProjectPostCommentService projectPostCommentService;

    @PostMapping
    @Operation(summary = "댓글 생성")
    public ResponseEntity<ApiResponse> createComment(@PathVariable Long postId,
        @RequestBody ProjectPostCommentRequest request, HttpServletRequest httpRequest,
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails) {
        String clientIp = getClientIp(httpRequest);
        projectPostCommentService.createComment(userDetails.getUser(), postId, request, clientIp);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.CREATED.value(), "댓글 생성 완료"));
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정")
    public ResponseEntity<ApiResponse> modifyComment(@PathVariable Long postId,
        @PathVariable Long commentId, @RequestBody ProjectPostCommentRequest request, @AuthenticationPrincipal AuthUserDetailsImpl userDetails)  {
        projectPostCommentService.modifyComment(userDetails.getUser(), postId, commentId, request);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "게시글이 수정되었습니다."));
    }

    @GetMapping
    @Operation(summary = "댓글 목록 조회")
    public ResponseEntity<List<ProjectPostCommentListResponse>> getCommentList(
        @PathVariable Long postId) {
        List<ProjectPostCommentListResponse> resultList = projectPostCommentService.getCommentList(
            postId);
        return ResponseEntity.ok(resultList);
    }


    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Long postId,
        @PathVariable Long commentId, @AuthenticationPrincipal AuthUserDetailsImpl userDetails) {
        projectPostCommentService.deleteComment(userDetails.getUser(), postId, commentId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "게시글이 삭제되었습니다."));
    }
}
