package com.spring.step3.security.manger.demo1;

import com.spring.step3.domain.vo.result.Result;
import com.spring.step3.security.config.properties.SecurityConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.method.MethodInvocationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;

/**
 * 处理方法调用后的授权检查
 * check()方法接收的是MethodInvocationResult对象，包含已执行方法的结果
 * 用于决定是否允许返回某个方法的结果(后置过滤)
 * 这是Spring Security较新的"后置授权"功能
 */
@Component
@RequiredArgsConstructor
public class PostAuthorizationManager implements AuthorizationManager<MethodInvocationResult> {

    private final SecurityConfigProperties securityConfigProperties;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, MethodInvocationResult methodInvocationResult) {
        Authentication authentication = authenticationSupplier.get();

        // 如果方法有 @PreAuthorize 注解，会先到这里
        if (authentication == null || !authentication.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        // 检查权限
        boolean granted = hasPermission(authentication, methodInvocationResult);
        return new AuthorizationDecision(granted);
    }

    private boolean hasPermission(Authentication authentication, MethodInvocationResult methodInvocationResult) {
        // 获取当前校验方法的返回值
        if (methodInvocationResult.getResult() instanceof Result<?> result) {
            // 拿到当前返回值中权限内容
            List<String> auths = result.getAuths();

            // 允许全局访问的 角色或权限
            List<String> adminAuthorities = securityConfigProperties.adminAuthorities;

            // 判断返回值中返回方法全新啊是否和用户权限匹配
            return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .anyMatch(auth ->
                            // 允许放行的角色或权限 和 匹配到的角色或权限
                            adminAuthorities.contains(auth) || auths.contains(auth)
                    );
        }

        // ❗这里可以设置自己的返回状态
        // ======================================
        // 默认返回 TRUE 是因为有可能当前方法不需要验证
        // 所以才设置默认返回为 TURE
        // ======================================
        return true;
    }
}