package com.welcommu.moduleservice.admininquiry;

import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryCommentListResponse;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryCommentRequest;
import java.util.List;

public interface AdminInquiryCommentService {

    void createAdminInquiryComment(User user, AdminInquiryCommentRequest request, Long inquiryId)
        throws CustomException;

    void modifyAdminInquiryComment(Long commentId, AdminInquiryCommentRequest request)
        throws CustomException;

    List<AdminInquiryCommentListResponse> getAdminInquiryCommentList(Long inquiryId);

    void deleteAdminInquiryComment(Long commentId) throws CustomException;
}