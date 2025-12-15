package com.spring.step3.controller.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "检查接口", description = "检查当前用户的权限信息")
@RestController
@RequestMapping("/api/security")
public class CheckController {

    @Operation(summary = "当前用户的信息", description = "当前用户的信息")
    @GetMapping("/current-user")
    public Authentication getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Current user: " + auth.getName());
        System.out.println("Authorities: " + auth.getAuthorities());
        return auth;
    }

    @Operation(summary = "获取用户详情", description = "获取用户详情")
    @GetMapping("user-detail")
    public UserDetails getCurrentUserDetail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        } else {
            return User.builder().username("未知").password("未知").build();
        }
    }

}
