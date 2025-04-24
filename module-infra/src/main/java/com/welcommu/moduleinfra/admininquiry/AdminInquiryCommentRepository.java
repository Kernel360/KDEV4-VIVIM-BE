package com.welcommu.moduleinfra.admininquiry;

import com.welcommu.moduledomain.admininquiry.AdminInquiryComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminInquiryCommentRepository extends JpaRepository<AdminInquiryComment, Long> {

    List<AdminInquiryComment> findAllByAdminInquiryIdAndDeletedAtIsNull(Long adminInquiry_id);
}
