package com.welcommu.moduleservice.admininquiry;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.admininquiry.AdminInquiry;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.admininquiry.AdminInquiryRepository;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminInquiryService {

    private final AdminInquiryRepository adminInquiryRepository;

    public void createAdminInquiry(User user, AdminInquiryRequest adminInquiryRequest) {
        AdminInquiry newInquiry = adminInquiryRequest.toEnity(user, adminInquiryRequest);

        adminInquiryRepository.save(newInquiry);
    }

    @Transactional
    public void modifyAdminInquiry(Long admininquiryId, User user,
        AdminInquiryRequest adminInquiryRequest) {
        AdminInquiry existingInquiry = adminInquiryRepository.findById(admininquiryId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_INQUIRY));

        existingInquiry.setInquiryType(adminInquiryRequest.getInquiryType());
        existingInquiry.setProjectId(adminInquiryRequest.getProjectId());
        existingInquiry.setTitle(adminInquiryRequest.getTitle());
        existingInquiry.setContent(adminInquiryRequest.getContent());
    }
    /*
    public List<AdminInquiryListResponse> getAllAdminInquirys() {
        List<AdminInquiry> adminInquiryList = adminInquiryRepository.findAllByDeletedAtIsNull();

        return adminInquiryList.stream()
            .map(AdminInquiryListResponse::from)
            .collect(Collectors.toList());

    }*/

}
