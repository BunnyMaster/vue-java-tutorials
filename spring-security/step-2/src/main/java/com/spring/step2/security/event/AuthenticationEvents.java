package com.spring.step2.security.event;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.security.authorization.event.AuthorizationGrantedEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
@Component
public class AuthenticationEvents {

    /**
     * 监听拒绝授权内容
     *
     * @param failure 授权失败
     */
    @EventListener
    public void onFailure(AuthorizationDeniedEvent<MethodInvocation> failure) {
        try {
            // getSource 和 getObject意思一样，一种是传入泛型自动转换一种是要手动转换
            Object source = failure.getSource();

            // 直接获取泛型对象
            MethodInvocation methodInvocation = failure.getObject();
            Method method = methodInvocation.getMethod();
            Object[] args = methodInvocation.getArguments();

            log.warn("方法调用被拒绝: {}.{}, 参数: {}",
                    method.getDeclaringClass().getSimpleName(),
                    method.getName(),
                    Arrays.toString(args));

            // 这里面的信息，和接口 /api/security/current-user 内容一样
            Authentication authentication = failure.getAuthentication().get();

            AuthorizationDecision authorizationDecision = failure.getAuthorizationDecision();
            // ExpressionAuthorizationDecision [granted=false, expressionAttribute=hasAuthority('ADMIN')]
            System.out.println(authorizationDecision);

            log.warn("授权失败 - 用户: {}, 权限: {}", authentication.getName(), authorizationDecision);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    /**
     * 监听授权的内容
     * 如果要监听授权成功的内容，这个内容可能相当的多，毕竟正常情况授权成功的内容还是比较多的。
     * 既然内容很多又要监听，如果真的需要，一定要处理好业务逻辑，不要被成功的消息淹没。
     *
     * @param success 授权成功
     */
    @EventListener
    public void onSuccess(AuthorizationGrantedEvent<MethodInvocation> success) {
    }
}