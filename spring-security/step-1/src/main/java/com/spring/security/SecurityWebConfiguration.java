package com.spring.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityWebConfiguration {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String[] permitAllUrls = {
                "/", "/doc.html/**",
                "/webjars/**", "/images/**", ".well-known/**", "favicon.ico", "/error/**",
                "/v3/api-docs/**"
        };

        http.authorizeHttpRequests(authorizeRequests ->
                        // 访问路径为 /api/** 时需要进行认证
                        authorizeRequests
                                .requestMatchers("/api/**").authenticated()
                                .requestMatchers(permitAllUrls).permitAll()
                )
                .formLogin(loginPage -> loginPage
                        // 自定义登录页路径
                        .loginPage("/login-page")
                        // 处理登录的URL（默认就是/login）
                        .loginProcessingUrl("/login")
                        // 登录成功跳转
                        .defaultSuccessUrl("/")
                        // 登录失败跳转
                        .failureUrl("/login-page?error=true")
                        .permitAll()
                )
                // 使用默认的登录
                // .formLogin(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login-page?logout=true")
                        .permitAll()
                );
        return http.build();
    }

}
