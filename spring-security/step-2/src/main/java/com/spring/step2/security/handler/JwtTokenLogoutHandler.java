package com.spring.step2.security.handler;

import com.alibaba.fastjson2.JSON;
import com.spring.step2.domain.vo.result.Result;
import com.spring.step2.domain.vo.result.ResultCodeEnum;
import com.spring.step2.security.service.JwtBearTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 实现注销处理器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenLogoutHandler implements LogoutHandler {

    private final JwtBearTokenService jwtBearTokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            String authorizationToken = request.getHeader("Authorization");
            if (StringUtils.hasText(authorizationToken)) {
                // 如果当前用户信息存在redis中可以通过这个进行退出
                String username = jwtBearTokenService.getUsernameFromToken(authorizationToken);
                log.info("username : {}", username);
            }

            Result<String> result = Result.success(ResultCodeEnum.SUCCESS_LOGOUT);
            // 转成JSON格式
            Object json = JSON.toJSON(result);

            // 返回响应
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(json);
            response.flushBuffer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
