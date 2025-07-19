package com.spring.step3.security.manger.demo2;

import com.spring.step3.security.config.properties.SecurityConfigProperties;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class PreAuthorizationManager implements AuthorizationManager<MethodInvocation> {

    private final SecurityConfigProperties securityConfigProperties;
    private final MethodSecurityExpressionHandler expressionHandler;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, MethodInvocation methodInvocation) {

        // 获取方法上的@PreAuthorize注解
        PreAuthorize preAuthorize = AnnotationUtils.findAnnotation(methodInvocation.getMethod(), PreAuthorize.class);

        if (preAuthorize == null) {
            // 没有注解默认放行
            return new AuthorizationDecision(true);
        }

        // 使用Spring的表达式解析器
        EvaluationContext ctx = expressionHandler.createEvaluationContext(authenticationSupplier.get(), methodInvocation);

        try {
            // 解析表达式并获取结果
            Expression expression = expressionHandler.getExpressionParser().parseExpression(preAuthorize.value());

            boolean granted = Boolean.TRUE.equals(expression.getValue(ctx, Boolean.class));

            // 如果表达式不通过，检查是否是admin
            if (!granted) {
                granted = isAdmin(authenticationSupplier.get());
            }

            return new AuthorizationDecision(granted);
        } catch (EvaluationException | ParseException e) {
            return new AuthorizationDecision(false);
        }
    }

    private boolean isAdmin(Authentication authentication) {
        return securityConfigProperties.getAdminAuthorities().stream()
                .anyMatch(auth -> authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(ga -> ga.equals(auth)));
    }
}