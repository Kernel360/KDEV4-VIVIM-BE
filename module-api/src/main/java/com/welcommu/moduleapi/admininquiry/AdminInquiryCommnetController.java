package com.welcommu.moduleapi.admininquiry;


import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduleservice.admininquiry.AdminInquiryCommentService;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryCommentListResponse;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryCommentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "관리자 문의 답변 API", description = "관리자 문의에 대한 답변을 생성, 전체 조회, 수정, 삭제 시킬 수 있습니다.")
public class AdminInquiryCommnetController {

    private final AdminInquiryCommentService adminInquiryCommentService;

    @PostMapping("/api/admininquiry/{admininquiryId}/comment")
    @Operation(summary = "문의에 대한 답변을 작성합니다.")
    public ResponseEntity<ApiResponse> createAdminInquiryComment(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
        @RequestBody AdminInquiryCommentRequest adminInquiryCommentRequest,
        @PathVariable Long admininquiryId) {

        adminInquiryCommentService.createAdminInquiryComment(userDetails.getUser(),
            adminInquiryCommentRequest, admininquiryId);

        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.CREATED.value(), "답변이 생성 되었습니다."));
    }

    @PutMapping("/api/admininquiry/{admininquiryId}/comment/{commentId}")
    @Operation(summary = "문의에 대한 답변을 수정합니다.")
    public ResponseEntity<ApiResponse> modifyAdminInquiryComment(@PathVariable Long commentId,
        @RequestBody AdminInquiryCommentRequest adminInquiryCommentRequest) {

        adminInquiryCommentService.modifyAdminInquiryComment(commentId,
            adminInquiryCommentRequest);
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.OK.value(), "답변이 수정 되었습니다."));
    }

    @GetMapping("/api/admininquiry/{admininquiryId}/comment")
    @Operation(summary = "모든 답변을 전체 조회 합니다.")
    public ResponseEntity<List<AdminInquiryCommentListResponse>> getAdminInquiryCommentList(
        @PathVariable Long admininquiryId) {
        return ResponseEntity.ok(
            adminInquiryCommentService.getAdminInquiryCommentList(admininquiryId));
    }

    @PatchMapping("/api/admininquiry/{admininquiryId}/comment/{commentId}/delete")
    @Operation(summary = "관리자가 답변를 삭제합니다.")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long commentId) {
        adminInquiryCommentService.deleteAdminInquiryComment(commentId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "답변이 삭제 되었습니다."));
    }
}
