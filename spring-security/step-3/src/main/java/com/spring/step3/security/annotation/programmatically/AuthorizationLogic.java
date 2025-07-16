package com.spring.step3.security.annotation.programmatically;

import org.springframework.stereotype.Component;

@Component("auth")
public class AuthorizationLogic {

    public boolean decide(String name) {
        System.out.println(name);
        // 直接使用name的实现
        return name.equalsIgnoreCase("user");
    }

}