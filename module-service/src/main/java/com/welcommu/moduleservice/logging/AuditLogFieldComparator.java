package com.welcommu.moduleservice.logging;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class AuditLogFieldComparator {
    public Map<String, String[]> compare(Object before, Object after) {
        Map<String, String[]> changes = new HashMap<>();

        if (before == null || after == null) {
            return changes;
        }

        Class<?> clazz = before.getClass();
        if (!clazz.equals(after.getClass())) {
            throw new IllegalArgumentException("비교 대상 클래스가 다릅니다: " + clazz + " vs " + after.getClass());
        }

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object beforeValue = field.get(before);
                Object afterValue = field.get(after);

                if (!Objects.equals(beforeValue, afterValue)) {
                    changes.put(field.getName(), new String[]{
                        String.valueOf(beforeValue),
                        String.valueOf(afterValue)
                    });
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("필드 접근 불가: " + field.getName(), e);
            }
        }

        return changes;
    }
}
