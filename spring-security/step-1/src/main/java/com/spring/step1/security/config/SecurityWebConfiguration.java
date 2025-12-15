package com.spring.step1.security.config;

import com.spring.step1.security.handler.SecurityAccessDeniedHandler;
import com.spring.step1.security.handler.SecurityAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
                "/swagger-ui/**", "/v3/api-docs/**"
        };

        http.authorizeHttpRequests(authorizeRequests ->
                        // 访问路径为 /api 时需要进行认证
                        authorizeRequests
                                .requestMatchers(permitAllUrls).permitAll()
                                .requestMatchers("/api/security/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/anonymous/**").anonymous()
                                // 会自动变成 ROLE_ADMIN
                                // .requestMatchers("/api/**").hasRole("ADMIN")
                                .requestMatchers("/api/**").hasAnyAuthority("all", "read")
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
                // 禁用表单登录
                // .formLogin(AbstractHttpConfigurer::disable)
                .logout(logout -> logout
                        .logoutSuccessUrl("/login-page?logout=true")
                        .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(configurer -> configurer
                        .accessDeniedHandler(new SecurityAccessDeniedHandler())
                        .authenticationEntryPoint(new SecurityAuthenticationEntryPoint())
                )
        ;

        return http.build();
    }
}