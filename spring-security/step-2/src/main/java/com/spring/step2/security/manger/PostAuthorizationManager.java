package com.spring.step2.security.manger;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.method.MethodInvocationResult;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * 处理方法调用后的授权检查
 * check()方法接收的是MethodInvocationResult对象，包含已执行方法的结果
 * 用于决定是否允许返回某个方法的结果(后置过滤)
 * 这是Spring Security较新的"后置授权"功能
 */
@Component
public class PostAuthorizationManager implements AuthorizationManager<MethodInvocationResult> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, MethodInvocationResult invocation) {
        return new AuthorizationDecision(true);
    }
}