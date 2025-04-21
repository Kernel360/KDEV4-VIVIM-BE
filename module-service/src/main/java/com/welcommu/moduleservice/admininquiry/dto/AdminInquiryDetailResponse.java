package com.welcommu.moduleservice.admininquiry.dto;

import com.welcommu.moduledomain.admininquiry.AdminInquiry;
import com.welcommu.moduledomain.admininquiry.AdminInquiryStatus;
import com.welcommu.moduledomain.admininquiry.AdminInquiryType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class AdminInquiryDetailResponse {

    private String title;
    private String content;
    private AdminInquiryType inquiryType;
    private AdminInquiryStatus inquiryStatus;
    private LocalDateTime createdAt;
    private String CreatorName;
    private Long projectId;
    private String projectName;


    public static AdminInquiryDetailResponse from(AdminInquiry inquiry) {
        String projectName = (inquiry.getProject() != null) ? inquiry.getProject().getName() : null;
        Long projectId = (inquiry.getProject() != null) ? inquiry.getProject().getId() : null;

        return new AdminInquiryDetailResponse(
            inquiry.getTitle(),
            inquiry.getContent(),
            inquiry.getInquiryType(),
            inquiry.getInquiryStatus(),
            inquiry.getCreatedAt(),
            inquiry.getCreator().getName(),
            projectId,
            projectName
        );
    }
}
