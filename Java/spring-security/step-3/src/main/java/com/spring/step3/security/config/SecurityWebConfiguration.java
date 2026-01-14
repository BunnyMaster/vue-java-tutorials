package com.spring.step3.security.config;

import com.spring.step3.security.config.properties.SecurityConfigProperties;
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
    private final SecurityConfigProperties pathsProperties;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // 前端段分离不需要---禁用明文验证
                // .httpBasic(AbstractHttpConfigurer::disable)
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
                // 如果要对部分接口做登录校验 或者 项目中需要使用粗粒度的 校验
                .authorizeHttpRequests(authorizeRequests ->
                        // 访问路径为 /api 时需要进行认证
                        authorizeRequests
                                // // 不认证登录接口
                                // .requestMatchers(pathsProperties.noAuthPaths.toArray(String[]::new)).permitAll()
                                // // ❗只认证 securedPaths 下的所有接口
                                // // =======================================================================
                                // // 也可以在这里写多参数传入，如："/api/**","/admin/**"
                                // // 但是在 Spring过滤器中，如果要放行不需要认证请求，但是需要认证的接口必需要携带token。
                                // // 做法是在这里定义要认证的接口，如果要做成动态可以放到数据库。
                                // // =======================================================================
                                // .requestMatchers(pathsProperties.securedPaths.toArray(String[]::new)).authenticated()
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