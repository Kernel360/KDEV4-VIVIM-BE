package com.welcommu.moduledomain.projectprogress;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DefaultProjectProgress {

    REQUIREMENTS("요구사항 정의"),
    WIREFRAME("화면설계"),
    DESIGN("디자인"),
    PUBLISHING("퍼블리싱"),
    DEVELOPMENT("개발"),
    INSPECTION("검수"),
    COMPLETED("완료");

    private final String label;

    public DefaultProjectProgress nextStep() {
        DefaultProjectProgress[] values = DefaultProjectProgress.values();
        int nextOrdinal = this.ordinal() + 1;
        return values[nextOrdinal];
    }
}
