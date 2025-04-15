package com.welcommu.modulecommon.logging;

import com.welcommu.modulecommon.logging.enums.ActionType;
import com.welcommu.modulecommon.logging.enums.TargetType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAudit {
    TargetType targetType();
    ActionType actionType();
}
