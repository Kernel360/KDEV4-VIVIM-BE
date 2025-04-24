package com.welcommu.modulerepository.user;

import com.welcommu.moduledomain.company.CompanyRole;
import com.welcommu.moduledomain.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final EntityManager em;

    @Override
    public Page<User> searchByConditions(String name, String email, String phone,
        Long companyId, CompanyRole companyRole,
        Boolean isDeleted, Pageable pageable) {

        StringBuilder sb = new StringBuilder("SELECT u FROM User u WHERE 1=1");
        StringBuilder countSb = new StringBuilder("SELECT COUNT(u) FROM User u WHERE 1=1");
        Map<String, Object> params = new HashMap<>();

        if (name != null && !name.isBlank()) {
            sb.append(" AND u.name LIKE :name");
            countSb.append(" AND u.name LIKE :name");
            params.put("name", "%" + name + "%");
        }
        if (email != null && !email.isBlank()) {
            sb.append(" AND u.email LIKE :email");
            countSb.append(" AND u.email LIKE :email");
            params.put("email", "%" + email + "%");
        }
        if (phone != null && !phone.isBlank()) {
            sb.append(" AND u.phone LIKE :phone");
            countSb.append(" AND u.phone LIKE :phone");
            params.put("phone", "%" + phone + "%");
        }
        if (companyId != null) {
            sb.append(" AND u.company.id = :companyId");
            countSb.append(" AND u.company.id = :companyId");
            params.put("companyId", companyId);
        }
        if (companyRole != null) {
            sb.append(" AND u.company.companyRole = :companyRole");
            countSb.append(" AND u.company.companyRole = :companyRole");
            params.put("companyRole", companyRole);
        }
        if (isDeleted != null) {
            sb.append(" AND u.isDeleted = :isDeleted");
            countSb.append(" AND u.isDeleted = :isDeleted");
            params.put("isDeleted", isDeleted);
        }

        // 정렬 처리
        if (!pageable.getSort().isEmpty()) {
            sb.append(" ORDER BY ");
            String sortClause = pageable.getSort().stream()
                .map(order -> "u." + order.getProperty() + " " + order.getDirection())
                .collect(Collectors.joining(", "));
            sb.append(sortClause);
        }

        TypedQuery<User> query = em.createQuery(sb.toString(), User.class);
        TypedQuery<Long> countQuery = em.createQuery(countSb.toString(), Long.class);
        params.forEach((k, v) -> {
            query.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<User> resultList = query.getResultList();
        Long total = countQuery.getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }
}
