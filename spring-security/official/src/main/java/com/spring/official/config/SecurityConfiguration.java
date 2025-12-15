package com.spring.official.config;

import com.spring.official.config.handler.SecurityAccessDeniedHandler;
import com.spring.official.config.handler.SecurityAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        http
                .requestCache(cache -> cache.requestCache(requestCache))
                // 如果禁用登录页会有弹窗形式的登录
                .formLogin(AbstractHttpConfigurer::disable)
                // 开启打开默认页会让登录
                // .formLogin(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/", "/*/*/login").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(configurer -> {
                    // 访问拒绝处理器
                    configurer.accessDeniedHandler(new SecurityAccessDeniedHandler());
                    // 认证入口点
                    configurer.authenticationEntryPoint(new SecurityAuthenticationEntryPoint());
                })
        ;
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

}
