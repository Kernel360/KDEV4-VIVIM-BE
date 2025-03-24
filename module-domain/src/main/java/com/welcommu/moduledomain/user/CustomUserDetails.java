package com.welcommu.moduledomain.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 여기서는 예시로 사용자의 역할을 "USER"로 설정합니다.
        // 실제로는 사용자 역할에 따라 다르게 설정할 수 있습니다.
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();  // 비밀번호 반환
    }

    @Override
    public String getUsername() {
        return user.getEmail();  // 이메일을 사용자 이름으로 사용
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // 계정 잠금 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // 자격 증명 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return user.getIsDeleted() == null || !user.getIsDeleted();  // 사용자가 삭제되지 않았으면 활성 상태
    }

    public User getUser() {
        return user;
    }
}
