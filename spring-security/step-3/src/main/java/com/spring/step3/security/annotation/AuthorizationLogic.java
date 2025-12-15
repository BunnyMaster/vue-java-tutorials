package com.spring.step3.security.annotation;

import com.spring.step3.security.config.properties.SecurityConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("auth")
@RequiredArgsConstructor
public class AuthorizationLogic {

    private final SecurityConfigProperties securityConfigProperties;

    /**
     * 基本权限检查
     */
    public boolean decide(String requiredAuthority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // 检查用户是否有指定权限或是admin
        boolean baseAuthority = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals(requiredAuthority));

        return baseAuthority || isAdmin(authentication);
    }

    /**
     * 检查是否是管理员
     */
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && isAdmin(authentication);
    }

    private boolean isAdmin(Authentication authentication) {
        return securityConfigProperties.getAdminAuthorities().stream()
                .anyMatch(auth -> authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(ga -> ga.equals(auth)));
    }

}