package com.welcommu.moduledomain.projectprogress;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DefaultProjectProgress {

    요구사항정의("요구사항 정의"),
    화면설계("화면 설계"),
    디자인("디자인"),
    퍼블리싱("퍼블리싱"),
    개발("개발"),
    검수("검수"),
    완료("완료");

    private final String label;

}