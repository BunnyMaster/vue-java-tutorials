package com.spring.step3.security.event;

import com.alibaba.fastjson2.JSON;
import com.spring.step3.config.context.BaseContext;
import com.spring.step3.domain.entity.AuthLogEntity;
import com.spring.step3.service.log.AuthLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.security.authorization.event.AuthorizationGrantedEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationEvents {

    private final AuthLogService authLogService;

    /**
     * 监听拒绝授权内容
     *
     * @param failure 授权失败
     */
    @EventListener
    public void onFailure(AuthorizationDeniedEvent<MethodInvocation> failure) {
        try {
            // 当前执行的方法
            MethodInvocation methodInvocation = failure.getObject();
            // 方法名称
            Method method = methodInvocation.getMethod();
            // 方法参数
            Object[] args = methodInvocation.getArguments();

            // 用户身份
            Authentication authentication = failure.getAuthentication().get();
            // 用户名
            String username = authentication.getName();
            // 决策结果
            AuthorizationDecision decision = failure.getAuthorizationDecision();

            // 获取请求上下文信息
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            AuthLogEntity authLog = new AuthLogEntity();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                authLog.setRequestIp(request.getRemoteAddr());
                authLog.setRequestMethod(request.getMethod());
                authLog.setRequestUri(request.getRequestURI());
            }

            // 构建日志实体
            authLog.setEventType("DENIED");
            authLog.setUsername(username);
            // 需要实现获取用户ID的方法
            authLog.setUserId(BaseContext.getUserId());
            authLog.setClassName(method.getDeclaringClass().getName());
            authLog.setMethodName(method.getName());
            authLog.setMethodParams(JSON.toJSONString(args));
            authLog.setRequiredAuthority(decision.toString());
            authLog.setUserAuthorities(JSON.toJSONString(authentication.getAuthorities()));
            authLog.setCreateUser(BaseContext.getUserId());

            // 保存到数据库
            authLogService.save(authLog);

        } catch (Exception e) {
            log.error("记录授权失败日志异常", e);
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