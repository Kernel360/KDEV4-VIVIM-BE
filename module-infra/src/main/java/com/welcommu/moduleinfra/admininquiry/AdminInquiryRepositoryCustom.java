package com.welcommu.moduleinfra.admininquiry;

import com.welcommu.moduledomain.admininquiry.AdminInquiry;
import com.welcommu.moduledomain.admininquiry.AdminInquiryStatus;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminInquiryRepositoryCustom {

    Page<AdminInquiry> searchByConditions(String name, String description,
        AdminInquiryStatus status,
        LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

}
