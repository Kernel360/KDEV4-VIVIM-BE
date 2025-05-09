package com.welcommu.moduleservice.company;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.BDDAssumptions.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.company.CompanyRole;
import com.welcommu.moduleinfra.company.CompanyRepository;
import com.welcommu.moduleinfra.user.UserRepository;
import com.welcommu.moduleservice.company.audit.CompanyAuditService;
import com.welcommu.moduleservice.company.dto.CompanyRequest;
import com.welcommu.moduleservice.company.dto.CompanySnapshot;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    CompanyRepository companyRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CompanyAuditService companyAuditService;

    @InjectMocks
    CompanyServiceImpl companyService;

    @Test
    void 회사_생성_테스트(){
        // given
        CompanyRequest companyRequest = mock(CompanyRequest.class);
        Company company = Company.builder()
            .name("커널360")
            .phone("010-1234-5678")
            .email("contact@kernel360.com")
            .address("서울시 강남구")
            .coOwner("홍길동")
            .businessNumber("1234567890")
            .companyRole(CompanyRole.ADMIN)
            .build();

        when(companyRequest.toEntity()).thenReturn(company);
        when(companyRepository.save(company)).thenReturn(company);

        Long userId = 1L;

        //when
        companyService.createCompany(companyRequest, userId);

        //then
        ArgumentCaptor<CompanySnapshot> snapshotCaptor = ArgumentCaptor.forClass(CompanySnapshot.class);
        verify(companyAuditService).createAuditLog(snapshotCaptor.capture(), eq(userId));

        CompanySnapshot captured = snapshotCaptor.getValue();
        assertThat(captured.getName()).isEqualTo("커널360");
        assertThat(captured.getEmail()).isEqualTo("contact@kernel360.com");
        assertThat(captured.getBusinessNumber()).isEqualTo("1234567890");
        assertThat(captured.getCompanyRole()).isEqualTo(CompanyRole.ADMIN);
    }

    @Test
    void 회사_삭제시_softDelete(){
        // given
        Long companyId = 1L;
        Long deleterId = 100L;

        Company company = Company.builder()
            .id(companyId)
            .name("커널360")
            .isDeleted(false)
            .deletedAt(null)
            .build();

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));

        // when
        companyService.softDeleteCompany(companyId, deleterId);

        // then
        assertThat(company.getIsDeleted()).isTrue();
        assertThat(company.getDeletedAt()).isNotNull();

        verify(companyAuditService).deleteAuditLog(any(CompanySnapshot.class), eq(deleterId));
    }


}