package com.spring.step3.security.handler;

import com.spring.step3.domain.vo.result.Result;
import com.spring.step3.domain.vo.result.ResultCodeEnum;
import com.spring.step3.exception.MyAuthenticationException;
import com.spring.step3.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        log.error("SecurityAuthenticationEntryPoint:{}", authException.getLocalizedMessage());
        Result<Object> result;

        // 自定义认证异常
        if (authException instanceof MyAuthenticationException) {
            result = Result.error(null, authException.getMessage());
            ResponseUtil.out(response, result);
        }

        // 未认证---未登录
        result = Result.error(authException.getMessage(), ResultCodeEnum.LOGIN_AUTH);
        ResponseUtil.out(response, result);
    }
}
