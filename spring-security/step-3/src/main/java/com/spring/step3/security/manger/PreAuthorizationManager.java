package com.spring.step3.security.manger;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * 处理方法调用前的授权检查
 * check()方法接收的是MethodInvocation对象，包含即将执行的方法调用信息
 * 用于决定是否允许执行某个方法
 * 这是传统的"前置授权"模式
 */
@Component
public class PreAuthorizationManager implements AuthorizationManager<MethodInvocation> {

    /**
     * 这里两个实现方法按照Security官方要求进行实现
     * <h4>类说明：</h4>
     * 下面的实现是对方法执行前进行权限校验的判断
     * <pre>
     *     <code>AuthorizationManager &ltMethodInvocation></code>
     * </pre>
     * 下面的这个是对方法执行后对权限的判断
     * <pre>
     *     <code>AuthorizationManager &ltMethodInvocationResult></code>
     * </pre>
     *
     * <h4>注意事项：</h4>
     * 将上述两个方法按照自定义的方式进行实现后，还需要禁用默认的。
     * <pre>
     * &#064;Configuration
     * &#064;EnableMethodSecurity(prePostEnabled = false)
     * class MethodSecurityConfig {
     *     &#064;Bean
     *     &#064;Role(BeanDefinition.ROLE_INFRASTRUCTURE)
     *    Advisor preAuthorize(MyAuthorizationManager manager) {
     * 		return AuthorizationManagerBeforeMethodInterceptor.preAuthorize(manager);
     *    }
     *
     *    &#064;Bean
     *    &#064;Role(BeanDefinition.ROLE_INFRASTRUCTURE)
     *    Advisor postAuthorize(MyAuthorizationManager manager) {
     * 		return AuthorizationManagerAfterMethodInterceptor.postAuthorize(manager);
     *    }
     * }
     * </pre>
     */
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, MethodInvocation methodInvocation) {
        Authentication authentication = authenticationSupplier.get();

        // 如果方法有 @PreAuthorize 注解，会先到这里
        if (authentication == null || !authentication.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        // 检查权限
        boolean granted = hasPermission(authentication, methodInvocation);
        return new AuthorizationDecision(granted);
    }

    private boolean hasPermission(Authentication authentication, MethodInvocation methodInvocation) {
        // 1. 获取方法上的权限注解（如果有）
        // 例如：@PreAuthorize("hasRole('ADMIN')") 或其他自定义注解

        // 2. 获取用户权限
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // 3. 实现你的权限逻辑
        // 这里简单示例：检查方法名是否包含在权限中
        String methodName = methodInvocation.getMethod().getName();
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                // ❗这里是忽略了大小写匹配的 admin 权限，如果包含 admin 无论大小写都可以放行
                .anyMatch(auth -> auth.equalsIgnoreCase("admin") || auth.equals(methodName));
    }
}