package com.spring.step3.security.config;

import com.spring.step3.security.filter.JwtAuthenticationFilter;
import com.spring.step3.security.handler.SecurityAccessDeniedHandler;
import com.spring.step3.security.handler.SecurityAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityWebConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 前端段分离不需要---禁用明文验证
                .httpBasic(AbstractHttpConfigurer::disable)
                // 前端段分离不需要---禁用默认登录页
                .formLogin(AbstractHttpConfigurer::disable)
                // 前端段分离不需要---禁用退出页
                .logout(AbstractHttpConfigurer::disable)
                // 前端段分离不需要---csrf攻击
                .csrf(AbstractHttpConfigurer::disable)
                // 跨域访问权限，如果需要可以关闭后自己配置跨域访问
                .cors(AbstractHttpConfigurer::disable)
                // 前后端分离不需要---因为是无状态的
                // .sessionManagement(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeRequests ->
                        // 访问路径为 /api 时需要进行认证
                        authorizeRequests
                                // 不认证登录接口
                                .requestMatchers("/*/login", "/api/security/**").permitAll()
                                // 只认证 /api/** 下的所有接口
                                .requestMatchers("/api/**").authenticated()
                                // 其余请求都放行
                                .anyRequest().permitAll()
                )
                .exceptionHandling(exception -> {
                    // 请求未授权接口
                    exception.authenticationEntryPoint(new SecurityAuthenticationEntryPoint());
                    // 没有权限访问
                    exception.accessDeniedHandler(new SecurityAccessDeniedHandler());
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }
}