package com.welcommu.moduleapi.admininquiry;


import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduleservice.admininquiry.AdminInquiryService;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "관리자 문의 API", description = "관리자 문의를 생성, 전체 조회, 상세 조회, 수정, 삭제 시킬 수 있습니다.")
public class AdminInquiryController {

    private final AdminInquiryService adminInquiryService;

    @PostMapping("/api/admininquiry")
    public ResponseEntity<ApiResponse> createAdminInquiry(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
        @RequestBody AdminInquiryRequest adminInquiryRequest) {

        adminInquiryService.createAdminInquiry(userDetails.getUser(), adminInquiryRequest);
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.OK.value(), "관리자 문의가 생성 되었습니다."));
    }

    @PatchMapping("/api/admininquiry/{admininquiryId}")
    public ResponseEntity<ApiResponse> modifyAdminInquiry(@PathVariable Long admininquiryId,
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails,
        @RequestBody AdminInquiryRequest adminInquiryRequest) {

        adminInquiryService.modifyAdminInquiry(admininquiryId, userDetails.getUser(),
            adminInquiryRequest);
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.OK.value(), "관리자 문의가 생성 되었습니다."));
    }

}
