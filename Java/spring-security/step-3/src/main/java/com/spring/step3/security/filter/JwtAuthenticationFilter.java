package com.spring.step3.security.filter;

import com.spring.step3.config.context.BaseContext;
import com.spring.step3.domain.vo.result.ResultCodeEnum;
import com.spring.step3.exception.AuthenticSecurityException;
import com.spring.step3.exception.MyAuthenticationException;
import com.spring.step3.security.config.properties.SecurityConfigProperties;
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
    private final SecurityConfigProperties pathsProperties;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 检查白名单路径
            if (isNoAuthPath(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 检查是否需要认证的路径
            if (!isSecurePath(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 验证 Token
            if (validToken(request, response, filterChain)) {
                filterChain.doFilter(request, response);
                return;
            }

            filterChain.doFilter(request, response);
        } catch (AuthenticSecurityException e) {
            // 直接处理认证异常，不再调用filterChain.doFilter()
            securityAuthenticationEntryPoint.commence(
                    request,
                    response,
                    new MyAuthenticationException(e.getMessage(), e)
            );
        } catch (RuntimeException e) {
            securityAuthenticationEntryPoint.commence(
                    request,
                    response,
                    new MyAuthenticationException("Authentication failed", e)
            );
        }
    }

    private boolean validToken(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws IOException, ServletException {
        // 验证Token
        String authHeader = request.getHeader("Authorization");

        // Token验证
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return true;
            // throw new AuthenticSecurityException(ResultCodeEnum.LOGIN_AUTH);
        }

        String jwtToken = authHeader.substring(7);

        if (jwtTokenService.isExpired(jwtToken)) {
            throw new AuthenticSecurityException(ResultCodeEnum.AUTHENTICATION_EXPIRED);
        }

        // 设置认证信息
        String username = jwtTokenService.getUsernameFromToken(jwtToken);
        Long userId = jwtTokenService.getUserIdFromToken(jwtToken);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            BaseContext.setUsername(username);
            BaseContext.setUserId(userId);
        }
        return false;
    }

    /**
     * 是否是不用验证的路径
     */
    private boolean isNoAuthPath(HttpServletRequest request) {
        RequestMatcher[] matchers = pathsProperties.noAuthPaths.stream()
                .map(AntPathRequestMatcher::new)
                .toArray(RequestMatcher[]::new);
        return new OrRequestMatcher(matchers).matches(request);
    }

    /**
     * 是否是要验证的路径
     */
    private boolean isSecurePath(HttpServletRequest request) {
        RequestMatcher[] matchers = pathsProperties.securedPaths.stream()
                .map(AntPathRequestMatcher::new)
                .toArray(RequestMatcher[]::new);
        return new OrRequestMatcher(matchers).matches(request);
    }
}