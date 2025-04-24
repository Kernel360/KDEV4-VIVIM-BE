package com.welcommu.modulerepository.company;

import com.welcommu.moduledomain.company.Company;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CompanyRepositoryImpl implements CompanyRepositoryCustom{

    private final EntityManager em;

    @Override
    public Page<Company> searchByConditions(String name, String businessNumber, String email, Boolean isDeleted, Pageable pageable) {
        StringBuilder sb = new StringBuilder("SELECT c FROM Company c WHERE 1=1");
        StringBuilder countSb = new StringBuilder("SELECT COUNT(c) FROM Company c WHERE 1=1");
        Map<String, Object> params = new HashMap<>();

        if (name != null && !name.isBlank()) {
            sb.append(" AND c.name LIKE :name");
            countSb.append(" AND c.name LIKE :name");
            params.put("name", "%" + name + "%");
        }

        if (businessNumber != null && !businessNumber.isBlank()) {
            sb.append(" AND c.businessNumber LIKE :businessNumber");
            countSb.append(" AND c.businessNumber LIKE :businessNumber");
            params.put("businessNumber", "%" + businessNumber + "%");
        }

        if (email != null && !email.isBlank()) {
            sb.append(" AND c.email LIKE :email");
            countSb.append(" AND c.email LIKE :email");
            params.put("email", "%" + email + "%");
        }

        if (isDeleted != null) {
            sb.append(" AND c.isDeleted = :isDeleted");
            countSb.append(" AND c.isDeleted = :isDeleted");
            params.put("isDeleted", isDeleted);
        }

        sb.append(" ORDER BY c.id DESC");

        TypedQuery<Company> query = em.createQuery(sb.toString(), Company.class);
        TypedQuery<Long> countQuery = em.createQuery(countSb.toString(), Long.class);

        params.forEach((k, v) -> {
            query.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Company> resultList = query.getResultList();
        Long total = countQuery.getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }
}
