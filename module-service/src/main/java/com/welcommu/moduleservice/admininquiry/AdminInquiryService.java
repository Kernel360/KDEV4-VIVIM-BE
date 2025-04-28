package com.welcommu.moduleservice.admininquiry;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.admininquiry.AdminInquiry;
import com.welcommu.moduledomain.admininquiry.AdminInquiryStatus;
import com.welcommu.moduledomain.admininquiry.AdminInquiryType;
import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleinfra.admininquiry.AdminInquiryRepository;
import com.welcommu.moduleinfra.project.ProjectRepository;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryDetailResponse;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryListResponse;
import com.welcommu.moduleservice.admininquiry.dto.AdminInquiryRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminInquiryService {

    private final AdminInquiryRepository adminInquiryRepository;
    private final ProjectRepository projectRepository;

    public void createAdminInquiry(User user, AdminInquiryRequest adminInquiryRequest) {

        Project project = null;

        if (adminInquiryRequest.getInquiryType() == AdminInquiryType.PROJECT) {
            project = projectRepository.findById(adminInquiryRequest.getProjectId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));
        }

        AdminInquiry newInquiry = adminInquiryRequest.toEntity(user, adminInquiryRequest, project);
        adminInquiryRepository.save(newInquiry);
    }

    @Transactional
    public void modifyAdminInquiry(Long admininquiryId, User user,
        AdminInquiryRequest adminInquiryRequest) {
        AdminInquiry existingInquiry = adminInquiryRepository.findById(admininquiryId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_INQUIRY));

        if (adminInquiryRequest.getInquiryType() == AdminInquiryType.PROJECT) {
            Project changedProject = projectRepository.findById(adminInquiryRequest.getProjectId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));
            existingInquiry.setProject(changedProject);
        }

        existingInquiry.setInquiryType(adminInquiryRequest.getInquiryType());
        existingInquiry.setTitle(adminInquiryRequest.getTitle());
        existingInquiry.setContent(adminInquiryRequest.getContent());
    }

    public List<AdminInquiryListResponse> getAdminInquiryList() {
        List<AdminInquiry> adminInquiryList = adminInquiryRepository.findAllByDeletedAtIsNull();
        return adminInquiryList.stream()
            .map(AdminInquiryListResponse::from)
            .collect(Collectors.toList());
    }

    public AdminInquiryDetailResponse getAdminInquiryDetail(Long admininquiryId) {
        AdminInquiry existingInquiry = adminInquiryRepository.findById(admininquiryId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_INQUIRY));
        return AdminInquiryDetailResponse.from(existingInquiry);
    }

    public List<AdminInquiryListResponse> getAdminInquiriesByUser(User user) {
        List<AdminInquiry> userInquiries = adminInquiryRepository.findByCreatorAndDeletedAtIsNull(
            user);
        return userInquiries.stream()
            .map(AdminInquiryListResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public void completeAdminInquiry(Long admininquiryId) {
        AdminInquiry existingInquiry = adminInquiryRepository.findById(admininquiryId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_INQUIRY));
        existingInquiry.setInquiryStatus(AdminInquiryStatus.COMPLETED);
    }

    @Transactional
    public void deleteAdminInquiry(Long admininquiryId) {
        AdminInquiry existingInquiry = adminInquiryRepository.findById(admininquiryId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_INQUIRY));
        existingInquiry.setDeletedAt(LocalDateTime.now());
    }
}
