package com.welcommu.moduledomain.company;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CompanyTest {

    @Test
    void 빌더로_회사_생성_성공(){
        Company company = Company.builder()
            .name("준서산업")
            .businessNumber("11111111")
            .address("경기도 괌명시")
            .phone("010=1111=1111")
            .email("pjs9177@naver.com")
            .companyRole(CompanyRole.ADMIN)
            .build();

        Assertions.assertThat(company.getName()).isEqualTo("준서산업");
        Assertions.assertThat(company.getCompanyRole()).isEqualTo(CompanyRole.ADMIN);
    }

    @Test
    void 수정시간_삭제시간_null(){
        Company company = Company.builder()
            .name("준서산업")
            .build();

        Assertions.assertThat(company.getModifiedAt()).isNull();
        Assertions.assertThat(company.getDeletedAt()).isNull();
    }
}