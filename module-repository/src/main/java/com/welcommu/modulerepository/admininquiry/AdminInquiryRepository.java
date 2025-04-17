package com.welcommu.modulerepository.admininquiry;

import com.welcommu.moduledomain.admininquiry.AdminInquiry;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminInquiryRepository extends JpaRepository<AdminInquiry, Long> {

    List<AdminInquiry> findAllByDeletedAtIsNull();

}
