package com.welcommu.moduleservice.logging;

import java.util.Map;
import org.springframework.stereotype.Component;

public interface AuditLogFieldComparator {
    Map<String, String[]> compare(Object before, Object after);
}
