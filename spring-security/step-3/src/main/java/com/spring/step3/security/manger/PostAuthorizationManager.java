package com.spring.step3.security.manger;

import com.spring.step3.domain.vo.result.Result;
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
public class PostAuthorizationManager implements AuthorizationManager<MethodInvocationResult> {

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
        // 1. 获取当前校验方法的返回值
        if (methodInvocationResult.getResult() instanceof Result<?> result) {
            // 拿到当前返回值中权限内容
            List<String> auths = result.getAuths();

            // 判断返回值中返回方法全新啊是否和用户权限匹配
            return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .anyMatch(auth ->
                            // ❗这里是忽略了大小写匹配的 admin 权限，如果包含 admin 无论大小写都可以放行
                            auth.equalsIgnoreCase("admin")
                                    || auths.contains(auth)
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