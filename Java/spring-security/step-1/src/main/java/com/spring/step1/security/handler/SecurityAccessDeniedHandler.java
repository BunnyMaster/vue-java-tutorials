package com.spring.step1.security.handler;

import com.alibaba.fastjson2.JSON;
import com.spring.step1.bean.vo.result.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Slf4j
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("CustomerAccessDeniedHandler:{}", accessDeniedException.getLocalizedMessage());

        Result<Object> result = Result.error(accessDeniedException.getMessage());

        Object json = JSON.toJSON(result);

        // 返回响应
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
    }
}
