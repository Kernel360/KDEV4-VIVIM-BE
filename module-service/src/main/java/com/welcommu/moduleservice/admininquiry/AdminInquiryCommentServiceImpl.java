package com.welcommu.moduleservice.admininquiry;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.admininquiry.AdminInquiry;
import com.welcommu.moduledomain.admininquiry.AdminInquiryComment;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleinfra.admininquiry.AdminInquiryCommentRepository;
import com.welcommu.moduleinfra.admininquiry.AdminInquiryRepository;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryCommentListResponse;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryCommentRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminInquiryCommentServiceImpl implements AdminInquiryCommentService {

    private final AdminInquiryCommentRepository adminInquiryCommentRepository;
    private final AdminInquiryRepository adminInquiryRepository;

    @Override
    public void createAdminInquiryComment(User user, AdminInquiryCommentRequest request,
        Long inquiryId) {
        AdminInquiry inquiry = adminInquiryRepository.findById(inquiryId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_INQUIRY));
        AdminInquiryComment comment = request.toEntity(user, request, inquiry);
        adminInquiryCommentRepository.save(comment);
    }

    @Override
    @Transactional
    public void modifyAdminInquiryComment(Long commentId, AdminInquiryCommentRequest request) {
        AdminInquiryComment existing = adminInquiryCommentRepository.findById(commentId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_INQUIRY_COMMENT));
        existing.setContent(request.getContent());
    }

    @Override
    public List<AdminInquiryCommentListResponse> getAdminInquiryCommentList(Long inquiryId) {
        return adminInquiryCommentRepository
            .findAllByAdminInquiryIdAndDeletedAtIsNull(inquiryId)
            .stream()
            .map(AdminInquiryCommentListResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAdminInquiryComment(Long commentId) {
        AdminInquiryComment existing = adminInquiryCommentRepository.findById(commentId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_INQUIRY_COMMENT));
        existing.setDeletedAt(LocalDateTime.now());
    }
}
