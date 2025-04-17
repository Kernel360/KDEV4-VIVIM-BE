// Repository 인터페이스
package com.welcommu.modulerepository.token;

import com.welcommu.moduledomain.token.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByUserIdAndTokenId(Long userId, String tokenId);

    void deleteByTokenId(String tokenId);

    void deleteAllByUserId(Long userId); // 사용자의 모든 토큰 삭제 (전체 로그아웃)
}