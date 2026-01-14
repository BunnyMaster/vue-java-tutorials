package com.spring.step2.security.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authorization.AuthorizationEventPublisher;
import org.springframework.security.authorization.SpringAuthorizationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 如果要监听授权和拒绝的授权需要发布一个像下面这样的事件
 * 之后使用 Spring 的  @EventListener
 */
@Component
public class SecurityAuthorizationPublisher {

    @Bean
    public AuthorizationEventPublisher authorizationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new SpringAuthorizationEventPublisher(applicationEventPublisher);
    }

}
