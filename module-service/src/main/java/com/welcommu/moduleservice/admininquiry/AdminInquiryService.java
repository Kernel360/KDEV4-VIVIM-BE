package com.welcommu.moduleservice.admininquiry;

import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.admininquiry.AdminInquiryStatus;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryDetailResponse;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryListResponse;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryRequest;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminInquiryService {

    void createAdminInquiry(User user, AdminInquiryRequest request) throws CustomException;

    void modifyAdminInquiry(Long inquiryId, User user, AdminInquiryRequest request)
        throws CustomException;

    List<AdminInquiryListResponse> getAdminInquiryList();

    AdminInquiryDetailResponse getAdminInquiryDetail(Long inquiryId) throws CustomException;

    List<AdminInquiryListResponse> getAdminInquiriesByUser(User user);

    Page<AdminInquiryListResponse> searchAdminInquiries(String title, String creatorName,
        LocalDate startDate, LocalDate endDate, AdminInquiryStatus status,
        Pageable pageable);

    void completeAdminInquiry(Long inquiryId) throws CustomException;

    void deleteAdminInquiry(Long inquiryId) throws CustomException;


}