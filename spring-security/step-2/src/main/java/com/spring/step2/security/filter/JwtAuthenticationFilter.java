package com.spring.step2.security.filter;

import com.spring.step2.config.context.BaseContext;
import com.spring.step2.domain.vo.result.ResultCodeEnum;
import com.spring.step2.exception.SecurityException;
import com.spring.step2.security.service.DbUserDetailService;
import com.spring.step2.security.service.JwtBearTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtBearTokenService jwtBearTokenService;
    private final DbUserDetailService userDetailsService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        // 如果当前请求不包含验证Token直接返回
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            // throw new SecurityException(ResultCodeEnum.LOGIN_AUTH);
            return;
        }

        // 当前请求的Token
        final String jwtToken = authHeader.substring(7);

        // 检查当前Token是否过期
        if (jwtBearTokenService.isTokenValid(jwtToken)) {
            throw new SecurityException(ResultCodeEnum.AUTHENTICATION_EXPIRED);
        }

        // 解析当前Token中的用户名
        final String username = jwtBearTokenService.getUsernameFromToken(jwtToken);
        final Long userId = jwtBearTokenService.getUserIdFromToken(jwtToken);

        // 当前用户名存在，并且 Security上下文为空，设置认证相关信息
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 调用用户信息进行登录
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 设置认证用户信息
            SecurityContextHolder.getContext().setAuthentication(authToken);
            BaseContext.setUsername(username);
            BaseContext.setUserId(userId);
        }

        filterChain.doFilter(request, response);
    }
}
