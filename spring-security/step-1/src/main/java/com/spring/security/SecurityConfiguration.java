package com.spring.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfiguration {

    /**
     * 添加内存用户
     *
     * @return {@link ConditionalOnMissingBean}
     */
    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder) {
        // 使用注入的密码加密器进行密码加密
        String generatedPassword = passwordEncoder.encode("123456");

        // 创建用户
        UserDetails userDetails1 = User.withUsername("bunny").password(generatedPassword).roles("USER").build();
        UserDetails userDetails2 = User.withUsername("rabbit").password(generatedPassword).roles("USER").build();

        // 返回内存中的用户
        return new InMemoryUserDetailsManager(userDetails1, userDetails2);
    }

    /**
     * 自定义密码加密器，可以自己实现，比如自己实现MD5的加密方式，但是Spring官方不推荐
     * 一般来说用Spring自带的加密器就完全可以了，Spring提供了如下的编码器。
     * 如果使用Spring的密码加密器，匹配密码时需要使用 matches 方法
     * {@link BCryptPasswordEncoder}:
     * <pre>
     *
     * </pre>
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

        // 自定义实现密码加密器
        // return new MD5PasswordEncoder();
    }
}
