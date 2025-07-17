package com.spring.step3.security.filter;

import com.spring.step3.config.context.BaseContext;
import com.spring.step3.domain.vo.result.ResultCodeEnum;
import com.spring.step3.exception.AuthenticSecurityException;
import com.spring.step3.exception.MyAuthenticationException;
import com.spring.step3.security.config.SecurityWebConfiguration;
import com.spring.step3.security.handler.SecurityAuthenticationEntryPoint;
import com.spring.step3.security.service.DbUserDetailService;
import com.spring.step3.security.service.JwtTokenService;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final DbUserDetailService userDetailsService;
    private final SecurityAuthenticationEntryPoint securityAuthenticationEntryPoint;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException, AuthenticSecurityException {
        // 先校验不需要认证的接口
        RequestMatcher[] requestNoAuthMatchers = SecurityWebConfiguration.noAuthPaths.stream()
                .map(AntPathRequestMatcher::new)
                .toArray(RequestMatcher[]::new);
        OrRequestMatcher noAuthRequestMatcher = new OrRequestMatcher(requestNoAuthMatchers);
        if (noAuthRequestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 获取需要认证的接口
        RequestMatcher[] requestSecureMatchers = SecurityWebConfiguration.securedPaths.stream()
                .map(AntPathRequestMatcher::new)
                .toArray(RequestMatcher[]::new);
        OrRequestMatcher secureRequestMatcher = new OrRequestMatcher(requestSecureMatchers);

        // 公开接口直接放行
        if (!secureRequestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        // 如果当前请求不包含验证Token直接返回
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            throw new AuthenticSecurityException(ResultCodeEnum.LOGIN_AUTH);
        }

        // 当前请求的Token
        final String jwtToken = authHeader.substring(7);

        try {
            // 检查当前Token是否过期
            if (jwtTokenService.isExpired(jwtToken)) {
                // 💡如果过期不需要进行判断和验证，需要直接放行可以像下面这样写
                // ===================================================
                // filterChain.doFilter(request, response);
                // return;
                // ===================================================
                throw new AuthenticSecurityException(ResultCodeEnum.AUTHENTICATION_EXPIRED);
            }

            // 解析当前Token中的用户名
            String username = jwtTokenService.getUsernameFromToken(jwtToken);
            Long userId = jwtTokenService.getUserIdFromToken(jwtToken);

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
        // ⚠️IMPORTANT:
        // ==========================================================================
        // 在 catch 块中，securityAuthenticationEntryPoint.commence() 已经处理了错误响应
        // 所以应该 直接返回，避免继续执行后续逻辑。
        // ==========================================================================
        catch (RuntimeException exception) {
            securityAuthenticationEntryPoint.commence(
                    request,
                    response,
                    new MyAuthenticationException(exception.getMessage(), exception)
            );
        }
    }
}
