package com.welcommu.moduleservice.company;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.welcommu.moduleapi.ModuleApiApplication;
import com.welcommu.moduleinfra.company.CompanyRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;


@SpringBootTest(classes = ModuleApiApplication.class)
public class CompanyModifyConcurrencyTest {
    @Autowired CompanyService companyService;
    @Autowired CompanyRepository companyRepository;
    @Autowired
    PlatformTransactionManager txManager;
    private Long companyId;

    @BeforeEach
    void setUp() {
        var c = new com.welcommu.moduledomain.company.Company();
        c.setName("원본사명");
        c.setBusinessNumber("1234");
        c.setEmail("orig@co.com");
        companyRepository.saveAndFlush(c);
        companyId = c.getId();
    }

    @Test
    void testOptimisticLockCollision() throws Exception {
        // 1) Barrier: 두 트랜잭션이 모두 읽기까지 완료되면 동시에 이어서 실행
        CyclicBarrier barrier = new CyclicBarrier(2);
        List<Exception> errors = Collections.synchronizedList(new ArrayList<>());

        // 2) TransactionTemplate 생성
        TransactionTemplate txTemplate = new TransactionTemplate(txManager);

        // 3) 두 Runnable 정의
        Runnable task1 = () -> {
            try {
                txTemplate.execute(status -> {
                    // 읽기
                    var company = companyRepository.findById(companyId).orElseThrow();
                    try {
                        barrier.await();                 // 둘 다 여기까지 도달할 때까지 대기
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (BrokenBarrierException e) {
                        throw new RuntimeException(e);
                    }
                    // 수정 & 즉시 flush
                    company.setName("T1-회사명");
                    companyRepository.saveAndFlush(company);
                    return null;
                });
            } catch (Exception e) {
                errors.add(e);
            }
        };

        Runnable task2 = () -> {
            try {
                txTemplate.execute(status -> {
                    var company = companyRepository.findById(companyId).orElseThrow();
                    try {
                        barrier.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (BrokenBarrierException e) {
                        throw new RuntimeException(e);
                    }
                    company.setEmail("t2@new.com");
                    companyRepository.saveAndFlush(company);
                    return null;
                });
            } catch (Exception e) {
                errors.add(e);
            }
        };

        // 4) 스레드 실행 & 대기
        Thread t1 = new Thread(task1), t2 = new Thread(task2);
        t1.start(); t2.start();
        t1.join(); t2.join();

        // 5) 검증: OptimisticLockingFailureException 이 하나쯤은 있어야 통과
        boolean hasCollision = errors.stream()
            .anyMatch(e -> e instanceof OptimisticLockingFailureException);
        assertTrue(hasCollision, "낙관적 락 충돌이 발생해야 합니다");
    }
}
