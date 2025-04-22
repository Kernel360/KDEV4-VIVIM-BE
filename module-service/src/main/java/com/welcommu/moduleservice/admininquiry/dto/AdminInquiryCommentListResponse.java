package com.welcommu.moduleservice.admininquiry.dto;

import com.welcommu.moduledomain.admininquiry.AdminInquiryComment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminInquiryCommentListResponse {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String CreatorName;

    public static AdminInquiryCommentListResponse from(AdminInquiryComment adminInquiryComment) {
        return new AdminInquiryCommentListResponse(
            adminInquiryComment.getId(),
            adminInquiryComment.getContent(),
            adminInquiryComment.getCreatedAt(),
            adminInquiryComment.getCreator().getName()
        );
    }
}
