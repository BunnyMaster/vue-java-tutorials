package com.spring.step1.security.config;

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

        // 创建用户 权限为只读
        UserDetails bunny = User.withUsername("bunny").password(generatedPassword).roles("USER").authorities("read").build();

        // 管理员可以查看全部
        UserDetails admin = User.withUsername("admin").password(generatedPassword).roles("ADMIN").authorities("all", "read").build();

        // 返回内存中的用户
        return new InMemoryUserDetailsManager(bunny, admin);
    }

    /**
     * 配置密码编码器Bean
     *
     * <p>Spring Security提供了多种密码编码器实现，推荐使用BCryptPasswordEncoder作为默认选择。</p>
     *
     * <p>特点：</p>
     * <ul>
     *   <li>BCryptPasswordEncoder - 使用bcrypt强哈希算法，自动加盐，是当前最推荐的密码编码器</li>
     *   <li>Argon2PasswordEncoder - 使用Argon2算法，抗GPU/ASIC攻击，但需要更多内存</li>
     *   <li>SCryptPasswordEncoder - 使用scrypt算法，内存密集型，抗硬件攻击</li>
     *   <li>Pbkdf2PasswordEncoder - 使用PBKDF2算法，较老但广泛支持</li>
     * </ul>
     *
     * <p>注意：不推荐使用MD5等弱哈希算法，Spring官方也不推荐自定义弱密码编码器。</p>
     *
     * @return PasswordEncoder 密码编码器实例
     * @see BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 其他编码器示例（根据需求选择一种）:
        // return new Argon2PasswordEncoder(16, 32, 1, 1 << 14, 2);
        // return new SCryptPasswordEncoder();
        // return new Pbkdf2PasswordEncoder("secret", 185000, 256);

        // 实际项目中只需返回一个密码编码器
        return new BCryptPasswordEncoder();
    }
}
