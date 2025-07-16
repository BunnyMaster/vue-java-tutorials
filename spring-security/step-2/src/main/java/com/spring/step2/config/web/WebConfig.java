package com.spring.step2.config.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final ThreadLocalCleanupInterceptor threadLocalCleanupInterceptor;

    /**
     * 因为实现了 ThreadLocalCleanupInterceptor
     * 要做就做到底，就在这里随便用了下
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(threadLocalCleanupInterceptor);
    }

    /**
     * 这里不涉及远程调用，只是复制时候懒得改了
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}