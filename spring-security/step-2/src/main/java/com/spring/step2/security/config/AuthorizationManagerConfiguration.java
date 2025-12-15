package com.spring.step2.security.config;

import org.springframework.context.annotation.Configuration;

@Configuration
// @EnableMethodSecurity(prePostEnabled = false)
public class AuthorizationManagerConfiguration {

    // @Bean
    // @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    // Advisor preAuthorize(PreAuthorizationManager manager) {
    //     return AuthorizationManagerBeforeMethodInterceptor.preAuthorize(manager);
    // }
    //
    // @Bean
    // @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    // Advisor postAuthorize(PostAuthorizationManager manager) {
    //     return AuthorizationManagerAfterMethodInterceptor.postAuthorize(manager);
    // }

}
