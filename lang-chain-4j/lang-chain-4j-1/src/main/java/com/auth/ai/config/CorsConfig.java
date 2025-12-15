package com.auth.ai.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 所有接口
        registry.addMapping("/**")
                // 允许所有来源
                .allowedOrigins("*")
                // 允许方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 允许所有头
                .allowedHeaders("*")
                // 允许凭证
                // .allowCredentials(true)
                // 预检请求缓存时间
                .maxAge(3600);
    }
}