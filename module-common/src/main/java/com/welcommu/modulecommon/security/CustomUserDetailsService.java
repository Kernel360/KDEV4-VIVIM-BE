package com.welcommu.modulecommon.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetailsService {

    UserDetails loadUserByUsername(String username);
}
