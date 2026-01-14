package com.spring.step2.config.web;

import com.spring.step2.config.context.BaseContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 这里和Security关系不大，数据库更新用户名会用以下，只是写下，没什么背的含义
 */
@Configuration
public class ThreadLocalCleanupInterceptor implements HandlerInterceptor {

    /**
     * 这个类用处并不是很大，Security也是有拦截器的(过滤器)
     */
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        return true;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        BaseContext.removeUser();
    }
}