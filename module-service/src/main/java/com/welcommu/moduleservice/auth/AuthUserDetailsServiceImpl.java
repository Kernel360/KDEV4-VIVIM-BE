package com.welcommu.moduleservice.auth;

import com.welcommu.modulecommon.security.CustomUserDetailsService;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleinfra.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("로그인 시도: " + email);  // 로그 추가

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return new AuthUserDetailsImpl(user);
    }
}
