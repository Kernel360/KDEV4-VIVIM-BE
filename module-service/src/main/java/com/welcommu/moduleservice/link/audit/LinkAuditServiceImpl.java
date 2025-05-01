package com.welcommu.moduleservice.link.audit;

import com.welcommu.moduledomain.logging.AuditLog;
import com.welcommu.moduledomain.logging.enums.ActionType;
import com.welcommu.moduledomain.logging.enums.TargetType;
import com.welcommu.moduleinfra.logging.AuditLogRepository;
import com.welcommu.moduleservice.link.dto.LinkListResponse;
import com.welcommu.moduleservice.logging.AuditLogFactory;
import com.welcommu.moduleservice.logging.AuditLogFieldComparator;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LinkAuditServiceImpl implements LinkAuditService{
    private final AuditLogRepository auditLogRepository;
    private final AuditLogFieldComparator auditLogFieldComparator;
    private final AuditLogFactory auditLogFactory;

    @Override
    public void createAuditLog(LinkListResponse user, Long userId) {
        AuditLog log = auditLogFactory.create(TargetType.LINK, user.getId(), ActionType.CREATE, userId);
        auditLogRepository.save(log);
    }

    @Override
    public void deleteAuditLog(LinkListResponse link, Long userId) {
        AuditLog log = auditLogFactory.create(TargetType.LINK, link.getId(), ActionType.DELETE, userId);
        auditLogRepository.save(log);
    }
}
