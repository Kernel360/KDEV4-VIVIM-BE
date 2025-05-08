package com.welcommu.moduleservice.admininquiry;

import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryDetailResponse;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryListResponse;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryRequest;
import java.util.List;

public interface AdminInquiryService {

    void createAdminInquiry(User user, AdminInquiryRequest request) throws CustomException;

    void modifyAdminInquiry(Long inquiryId, User user, AdminInquiryRequest request)
        throws CustomException;

    List<AdminInquiryListResponse> getAdminInquiryList();

    AdminInquiryDetailResponse getAdminInquiryDetail(Long inquiryId) throws CustomException;

    List<AdminInquiryListResponse> getAdminInquiriesByUser(User user);

    void completeAdminInquiry(Long inquiryId) throws CustomException;

    void deleteAdminInquiry(Long inquiryId) throws CustomException;
}