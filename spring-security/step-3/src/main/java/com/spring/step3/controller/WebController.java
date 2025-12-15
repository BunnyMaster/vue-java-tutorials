package com.spring.step3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "loginPage";
    }

    @GetMapping("/user")
    public String showUserPage() {
        return "userPage";
    }

    @GetMapping("/role")
    public String showRolePage() {
        return "rolePage";
    }

    @GetMapping("/permission")
    public String showPermissionPage() {
        return "permissionPage";
    }
}
