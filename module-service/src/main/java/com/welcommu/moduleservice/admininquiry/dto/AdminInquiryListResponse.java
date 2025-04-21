package com.welcommu.moduleservice.admininquiry.dto;

import com.welcommu.moduledomain.admininquiry.AdminInquiry;
import com.welcommu.moduledomain.admininquiry.AdminInquiryStatus;
import com.welcommu.moduledomain.admininquiry.AdminInquiryType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminInquiryListResponse {

    private Long id;
    private String title;
    private String content;
    private AdminInquiryType inquiryType;
    private AdminInquiryStatus inquiryStatus;
    private LocalDateTime createdAt;
    private String CreatorName;

    public static AdminInquiryListResponse from(AdminInquiry adminInquiry) {
        return new AdminInquiryListResponse(
            adminInquiry.getId(),
            adminInquiry.getTitle(),
            adminInquiry.getContent(),
            adminInquiry.getInquiryType(),
            adminInquiry.getInquiryStatus(),
            adminInquiry.getCreatedAt(),
            adminInquiry.getCreator().getName()
        );
    }
}
