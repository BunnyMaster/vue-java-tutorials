package com.spring.step3.security.annotation;

import org.springframework.stereotype.Component;

@Component("auth")
public class AuthorizationLogic {

    public boolean decide(String name) {
        // 直接使用name的实现
        // System.out.println(name);
        return name.equalsIgnoreCase("user");
    }

}