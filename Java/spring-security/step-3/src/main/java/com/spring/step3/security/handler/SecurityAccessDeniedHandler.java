package com.spring.step3.security.handler;

import com.spring.step3.domain.vo.result.Result;
import com.spring.step3.domain.vo.result.ResultCodeEnum;
import com.spring.step3.utils.ResponseUtil;
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
        log.error("SecurityAccessDeniedHandler:{}", accessDeniedException.getLocalizedMessage());

        // 无权访问接口
        Result<Object> result = Result.error(accessDeniedException.getMessage(), ResultCodeEnum.FAIL_NO_ACCESS_DENIED);
        ResponseUtil.out(response, result);
    }
}
