package com.welcommu.moduleservice.link.audit;

import com.welcommu.moduleservice.link.dto.LinkListResponse;

public interface LinkAuditService {
    void createAuditLog(LinkListResponse user, Long userId);
    void deleteAuditLog(LinkListResponse user, Long userId);
}
