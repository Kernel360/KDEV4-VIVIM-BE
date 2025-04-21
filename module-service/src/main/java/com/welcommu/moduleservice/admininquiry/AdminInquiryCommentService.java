package com.welcommu.moduleservice.admininquiry;


import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.admininquiry.AdminInquiry;
import com.welcommu.moduledomain.admininquiry.AdminInquiryComment;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.admininquiry.AdminInquiryCommentRepository;
import com.welcommu.modulerepository.admininquiry.AdminInquiryRepository;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryCommentListResponse;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryCommentRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminInquiryCommentService {

    private final AdminInquiryCommentRepository adminInquiryCommentRepository;
    private final AdminInquiryRepository adminInquiryRepository;

    public void createAdminInquiryComment(User user,
        AdminInquiryCommentRequest adminInquiryCommentRequest, Long adminInquiryId) {
        AdminInquiry existingInquiry = adminInquiryRepository.findById(adminInquiryId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_INQUIRY));
        AdminInquiryComment newInquiryComment = adminInquiryCommentRequest.toEntity(user,
            adminInquiryCommentRequest, existingInquiry);
        adminInquiryCommentRepository.save(newInquiryComment);
    }

    @Transactional
    public void modifyAdminInquiryComment(Long commentId,
        AdminInquiryCommentRequest adminInquiryCommentRequest) {
        AdminInquiryComment existingInquiryComment = adminInquiryCommentRepository.findById(
                commentId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_INQUIRY_COMMENT));
        existingInquiryComment.setContent(adminInquiryCommentRequest.getContent());
    }

    public List<AdminInquiryCommentListResponse> getAdminInquiryCommentList(Long adminInquiryId) {
        List<AdminInquiryComment> adminInquiryList = adminInquiryCommentRepository.findAllByAdminInquiryIdAndDeletedAtIsNull(
            adminInquiryId);
        return adminInquiryList.stream()
            .map(AdminInquiryCommentListResponse::from)
            .collect(Collectors.toList());
    }
}
