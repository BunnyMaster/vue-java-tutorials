package com.spring.step2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/login-page")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/user")
    public String showUserPage() {
        return "user/index";
    }
}
