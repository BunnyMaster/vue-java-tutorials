package com.spring.context;

import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextHolderTest {

    @Test
    void testSecurityContextHolder() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new TestingAuthenticationToken("username", "password", "ROLE_USER");
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication contextAuthentication = securityContext.getAuthentication();
        System.out.println(JSON.toJSON(contextAuthentication));
    }
}
