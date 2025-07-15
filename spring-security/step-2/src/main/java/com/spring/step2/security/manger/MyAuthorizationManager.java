package com.spring.step2.security.manger;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * 处理方法调用前的授权检查
 * check()方法接收的是MethodInvocation对象，包含即将执行的方法调用信息
 * 用于决定是否允许执行某个方法
 * 这是传统的"前置授权"模式
 */
@Component
public class MyAuthorizationManager implements AuthorizationManager<MethodInvocation> {
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, MethodInvocation invocation) {
        return new AuthorizationDecision(true);
    }
}