// Repository 인터페이스
package com.welcommu.moduleinfra.token;

import com.welcommu.moduledomain.auth.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUserIdAndTokenId(Long userId, String tokenId);

    void deleteByTokenId(String tokenId);

    void deleteAllByUserId(Long userId); // 사용자의 모든 토큰 삭제 (전체 로그아웃)
}