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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminInquiryServiceImpl implements AdminInquiryService {

    private final AdminInquiryRepository adminInquiryRepository;
    private final ProjectRepository projectRepository;

    @Override
    public void createAdminInquiry(User user, AdminInquiryRequest request) {
        Project project = null;
        if (request.getInquiryType() == AdminInquiryType.PROJECT) {
            project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));
        }
        AdminInquiry newInquiry = request.toEntity(user, request, project);
        adminInquiryRepository.save(newInquiry);
    }

    @Override
    @Transactional
    public void modifyAdminInquiry(Long inquiryId, User user, AdminInquiryRequest request) {
        AdminInquiry inquiry = adminInquiryRepository.findById(inquiryId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_INQUIRY));

        if (request.getInquiryType() == AdminInquiryType.PROJECT) {
            Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));
            inquiry.setProject(project);
        }
        inquiry.setInquiryType(request.getInquiryType());
        inquiry.setTitle(request.getTitle());
        inquiry.setContent(request.getContent());
    }

    @Override
    public List<AdminInquiryListResponse> getAdminInquiryList() {
        return adminInquiryRepository.findAllByDeletedAtIsNull()
            .stream()
            .map(AdminInquiryListResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public AdminInquiryDetailResponse getAdminInquiryDetail(Long inquiryId) {
        AdminInquiry inquiry = adminInquiryRepository.findById(inquiryId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_INQUIRY));
        return AdminInquiryDetailResponse.from(inquiry);
    }

    @Override
    public List<AdminInquiryListResponse> getAdminInquiriesByUser(User user) {
        return adminInquiryRepository.findByCreatorAndDeletedAtIsNull(user)
            .stream()
            .map(AdminInquiryListResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public Page<AdminInquiryListResponse> searchAdminInquiries(String title, String creatorName,
        LocalDate startDate, LocalDate endDate, AdminInquiryStatus status,
        Pageable pageable) {

        LocalDateTime startDateTime = null;
        if (startDate != null) {
            startDateTime = startDate.atTime(LocalTime.MIN);
        }

        LocalDateTime endDateTime = null;
        if (endDate != null) {
            endDateTime = endDate.atTime(LocalTime.MAX);
        }

        return adminInquiryRepository.searchByConditions(title, creatorName,
                status, startDateTime, endDateTime, pageable)
            .map(AdminInquiryListResponse::from);
    }

    @Override
    @Transactional
    public void completeAdminInquiry(Long inquiryId) {
        AdminInquiry inquiry = adminInquiryRepository.findById(inquiryId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_INQUIRY));
        inquiry.setInquiryStatus(AdminInquiryStatus.COMPLETED);
    }

    @Override
    @Transactional
    public void deleteAdminInquiry(Long inquiryId) {
        AdminInquiry inquiry = adminInquiryRepository.findById(inquiryId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_INQUIRY));
        inquiry.setDeletedAt(LocalDateTime.now());
    }
}