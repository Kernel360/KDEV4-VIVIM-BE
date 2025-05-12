package com.welcommu.moduleinfra.admininquiry;

import com.welcommu.moduledomain.admininquiry.AdminInquiry;
import com.welcommu.moduledomain.admininquiry.AdminInquiryStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class AdminInquiryRepositoryCustomImpl implements AdminInquiryRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<AdminInquiry> searchByConditions(
        String title,
        String creatorName,
        AdminInquiryStatus status,
        LocalDateTime startDate,  // 변경
        LocalDateTime endDate,    // 변경
        Pageable pageable
    ) {
        // 1) 동적 JPQL 문자열 조립
        StringBuilder jpql = new StringBuilder(
            "SELECT a FROM AdminInquiry a JOIN a.creator u WHERE a.deletedAt IS NULL");
        StringBuilder countJpql = new StringBuilder(
            "SELECT COUNT(a) FROM AdminInquiry a JOIN a.creator u WHERE a.deletedAt IS NULL");
        Map<String, Object> params = new HashMap<>();

        if (title != null && !title.isBlank()) {
            log.info("title");
            jpql.append(" AND a.title LIKE :title");
            countJpql.append(" AND a.title LIKE :title");
            params.put("title", "%" + title + "%");
        }
        if (creatorName != null && !creatorName.isBlank()) {
            log.info("creator name");
            jpql.append(" AND u.name LIKE :creatorName");
            countJpql.append(" AND u.name LIKE :creatorName");
            params.put("creatorName", "%" + creatorName + "%");
        }
        if (status != null) {
            jpql.append(" AND a.inquiryStatus = :status");
            countJpql.append(" AND a.inquiryStatus = :status");
            params.put("status", status);
        }
        if (startDate != null) {
            log.info("startDate");
            jpql.append(" AND a.createdAt >= :startDate");
            countJpql.append(" AND a.createdAt >= :startDate");
            params.put("startDate", startDate);
        }
        if (endDate != null) {
            log.info("endDate");
            jpql.append(" AND a.createdAt <= :endDate");
            countJpql.append(" AND a.createdAt <= :endDate");
            params.put("endDate", endDate);
        }

        // 정렬: Pageable 의 Sort 정보 반영 (없으면 기본 createdAt DESC)
        if (pageable.getSort().isSorted()) {
            jpql.append(" ORDER BY ");
            String orderClause = pageable.getSort().stream()
                .filter(o -> List.of("title", "createdAt", "modifiedAt", "inquiryStatus")
                    .contains(o.getProperty()))
                .map(o -> "a." + o.getProperty() + " " + o.getDirection().name())
                .collect(Collectors.joining(", "));
            jpql.append(orderClause);
        } else {
            jpql.append(" ORDER BY a.createdAt DESC");
        }

        // 2) TypedQuery 생성 및 파라미터 바인딩
        TypedQuery<AdminInquiry> query = em.createQuery(jpql.toString(), AdminInquiry.class);
        TypedQuery<Long> countQuery = em.createQuery(countJpql.toString(), Long.class);
        params.forEach((k, v) -> {
            query.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        // 3) 페이징
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<AdminInquiry> content = query.getResultList();
        Long total = countQuery.getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }

}