package com.welcommu.moduleservice.admininquiry.dto;

import com.welcommu.moduledomain.admininquiry.AdminInquiry;
import com.welcommu.moduledomain.admininquiry.AdminInquiryComment;
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
public class AdminInquiryCommentRequest {

    String content;

    public AdminInquiryComment toEntity(User user,
        AdminInquiryCommentRequest adminInquiryCommentRequest, AdminInquiry adminInquiry) {
        return new AdminInquiryComment().builder()
            .createdAt(LocalDateTime.now())
            .content(adminInquiryCommentRequest.content)
            .creator(user)
            .adminInquiry(adminInquiry)
            .build();
    }

}
