package com.spring.step2.security.handler;

import com.alibaba.fastjson2.JSON;
import com.spring.step2.domain.vo.result.Result;
import com.spring.step2.domain.vo.result.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("SecurityAuthenticationEntryPoint:{}", authException.getLocalizedMessage());

        // 未认证---未登录
        Result<Object> result = Result.error(authException.getMessage(), ResultCodeEnum.LOGIN_AUTH);

        // 将错误的请求转成JSON
        Object json = JSON.toJSON(result);

        // 返回响应
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
    }
}
