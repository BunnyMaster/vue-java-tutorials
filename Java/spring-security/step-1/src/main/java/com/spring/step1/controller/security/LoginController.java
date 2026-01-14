package com.spring.step1.controller.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/login-page")
    public String showLoginPage() {
        return "login";
    }
}