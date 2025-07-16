package com.spring.step3.security.handler;

import com.spring.step3.domain.vo.result.Result;
import com.spring.step3.domain.vo.result.ResultCodeEnum;
import com.spring.step3.security.service.JwtBearTokenService;
import com.spring.step3.utils.ResponseUtil;
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
        String authorizationToken = request.getHeader("Authorization");
        if (StringUtils.hasText(authorizationToken)) {
            // 如果当前用户信息存在redis中可以通过这个进行退出
            String username = jwtBearTokenService.getUsernameFromToken(authorizationToken);
            log.info("username : {}", username);
        }

        Result<Object> result = Result.success(ResultCodeEnum.SUCCESS_LOGOUT);
        ResponseUtil.out(response, result);
    }
}
