package com.welcommu.moduleservice.user.audit;

import com.welcommu.moduleservice.user.dto.UserSnapshot;

public interface UserAuditService {
    void createAuditLog(UserSnapshot user, Long userId);
    void modifyAuditLog(UserSnapshot before, UserSnapshot after, Long userId);
    void deleteAuditLog(UserSnapshot user, Long userId);
}
