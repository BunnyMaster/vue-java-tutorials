package com.spring.step2.security.handler;

import com.spring.step2.domain.vo.result.Result;
import com.spring.step2.domain.vo.result.ResultCodeEnum;
import com.spring.step2.utils.ResponseUtil;
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
        ResponseUtil.out(response, result);
    }
}
