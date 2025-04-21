package com.welcommu.moduleapi.admininquiry;


import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduleservice.admininquiry.AdminInquiryService;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryDetailResponse;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryListResponse;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryRequest;
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
@Tag(name = "관리자 문의 API", description = "관리자 문의를 생성, 전체 조회, 상세 조회, 수정, 삭제 시킬 수 있습니다.")
public class AdminInquiryController {


    private final AdminInquiryService adminInquiryService;

    @PostMapping("/api/admininquiry")
    @Operation(summary = "관리자 문의를 생성합니다.")
    public ResponseEntity<ApiResponse> createAdminInquiry(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
        @RequestBody AdminInquiryRequest adminInquiryRequest) {

        adminInquiryService.createAdminInquiry(userDetails.getUser(), adminInquiryRequest);
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.OK.value(), "관리자 문의가 생성 되었습니다."));
    }

    @PutMapping("/api/admininquiry/{admininquiryId}")
    @Operation(summary = "관리자 문의를 수정합니다.")
    public ResponseEntity<ApiResponse> modifyAdminInquiry(@PathVariable Long admininquiryId,
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
        @RequestBody AdminInquiryRequest adminInquiryRequest) {

        adminInquiryService.modifyAdminInquiry(admininquiryId, userDetails.getUser(),
            adminInquiryRequest);
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.OK.value(), "관리자 문의가 생성 되었습니다."));
    }

    @GetMapping("/api/admininquiry")
    @Operation(summary = "관리자가 모든 관리자 문의를 전체 조회 합니다.")
    public ResponseEntity<List<AdminInquiryListResponse>> getAdminInquiryList() {
        return ResponseEntity.ok(adminInquiryService.getAdminInquiryList());
    }

    @GetMapping("/api/admininquiry/{admininquiryId}")
    @Operation(summary = "관리자 문의를 세부 조회 합니다.")
    public ResponseEntity<AdminInquiryDetailResponse> getAdminInquiryDetail(
        @PathVariable Long admininquiryId) {
        return ResponseEntity.ok(adminInquiryService.getAdminInquiryDetail(admininquiryId));
    }

    @GetMapping("/api/user/admininquiry")
    @Operation(summary = "유저가 자신이 작성한 관리자 문의 전체를 조회합니다.")
    public ResponseEntity<List<AdminInquiryListResponse>> getUserAdminInquiryList(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails) {

        List<AdminInquiryListResponse> inquiries = adminInquiryService.getUserAdminInquiries(
            userDetails.getUser());
        return ResponseEntity.ok(inquiries);
    }

    @PatchMapping("/api/admininquiry/{admininquiryId}/complete")
    @Operation(summary = "관리자가 문의를 답변완료 상태로 변경시킵니다.")
    public ResponseEntity<ApiResponse> completeAdminInquiry(
        @PathVariable Long admininquiryId) {

        adminInquiryService.completeAdminInquiry(admininquiryId);
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.OK.value(), "관리자 문의가 답변완료 되었습니다."));

    }

    @PatchMapping("/api/admininquiry/{admininquiryId}/delete")
    @Operation(summary = "관리자가 문의를 삭제합니다.")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long admininquiryId) {
        adminInquiryService.deleteAdminInquiry(admininquiryId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "문의가 삭제 되었습니다."));
    }
}


