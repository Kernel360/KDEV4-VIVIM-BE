package com.welcommu.moduleservice.admininquiry.dto;

import com.welcommu.moduledomain.admininquiry.AdminInquiry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminInquiryListResponse {

    private String title;
    private String content;

    public static AdminInquiryListResponse from(AdminInquiry adminInquiry) {
        return new AdminInquiryListResponse(
            adminInquiry.getTitle(),
            adminInquiry.getContent()
        );
    }
}
