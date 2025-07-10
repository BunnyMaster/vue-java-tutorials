package com.spring.config.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated()
                )
        ;
        return http.build();
    }

    /**
     * 添加内存用户
     *
     * @return {@link ConditionalOnMissingBean}
     */
    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder) {

        String generatedPassword = passwordEncoder.encode("123456");
        return new InMemoryUserDetailsManager(User.withUsername("bunny")
                .password(generatedPassword).roles("USER").build());
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
    }
}
