package com.spring.step2.security.config;

import com.spring.step2.security.handler.SecurityAccessDeniedHandler;
import com.spring.step2.security.handler.SecurityAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityWebConfiguration {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests ->
                        // 访问路径为 /api 时需要进行认证
                        authorizeRequests
                                // 忽略认证接口
                                .requestMatchers("/api/security/**").permitAll()
                                // 只认证 /api/** 下的所有接口
                                .requestMatchers("/api/**").authenticated()
                                // 其余请求都放行
                                .anyRequest().permitAll()
                )
                // 使用默认的登录
                // .formLogin(Customizer.withDefaults())
                // 禁用表单登录
                // .formLogin(AbstractHttpConfigurer::disable)
                .formLogin(loginPage -> loginPage
                        // 自定义登录页路径
                        .loginPage("/login")
                        // 处理登录的URL（默认就是/login）
                        .loginProcessingUrl("/login")
                        // 登录成功跳转
                        .defaultSuccessUrl("/")
                        // 登录失败跳转
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/logout")
                        .permitAll()
                )
                .exceptionHandling(exception -> {
                    // 请求未授权接口
                    exception.authenticationEntryPoint(new SecurityAuthenticationEntryPoint());
                    // 没有权限访问
                    exception.accessDeniedHandler(new SecurityAccessDeniedHandler());
                })
        ;

        return http.build();
    }
}