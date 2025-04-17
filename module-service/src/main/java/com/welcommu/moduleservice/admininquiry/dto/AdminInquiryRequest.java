package com.welcommu.moduleservice.admininquiry.dto;

import com.welcommu.moduledomain.admininquiry.AdminInquiry;
import com.welcommu.moduledomain.admininquiry.AdminInquiryType;
import com.welcommu.moduledomain.user.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminInquiryRequest {

    private AdminInquiryType inquiryType;
    private Long projectId;
    private String title;
    private String content;

    public AdminInquiry toEnity(User user, AdminInquiryRequest inquiryRequest) {
        return AdminInquiry.builder()
            .inquiryType(inquiryRequest.getInquiryType())
            .projectId(inquiryRequest.getProjectId())
            .title(inquiryRequest.getTitle())
            .content(inquiryRequest.getContent())
            .createdAt(LocalDateTime.now())
            .creator(user)
            .build();
    }

}
