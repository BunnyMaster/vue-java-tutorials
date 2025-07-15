package com.spring.step2.security.config;

import com.spring.step2.security.service.DbUserDetailService;
import com.spring.step2.security.service.JwtBearTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
class JwtBearTokenServiceTest {

    private String token;

    @Autowired
    private JwtBearTokenService jwtBearTokenService;

    @Autowired
    private DbUserDetailService dbUserDetailService;

    @Test
    void createToken() {
        long userId = 1944384432521744386L;
        List<String> roles = dbUserDetailService.findUserRolesByUserId(userId);
        List<String> permissions = dbUserDetailService.findPermissionByUserId(userId);

        String token = jwtBearTokenService.createToken(userId,
                "Bunny",
                roles,
                permissions);
        this.token = token;
        System.out.println(token);
    }

    @Test
    void getUsernameFromToken() {
        createToken();

        String username = jwtBearTokenService.getUsernameFromToken(token);
        System.out.println(username);

        Long userId = jwtBearTokenService.getUserIdFromToken(token);
        System.out.println(userId);

        Map<String, Object> map = jwtBearTokenService.getMapByToken(token);

        List<?> roles = (List<?>) map.get("roles");
        System.out.println(roles);

        // 安全转换 permissions
        List<?> permissions = (List<?>) map.get("permissions");
        System.out.println(permissions);
    }
}