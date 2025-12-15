package com.spring.step3.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.method.PrePostTemplateDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration {

    /**
     * 注册一个用于Spring Security预授权/后授权的模板元注解默认配置Bean。
     *
     * <p>该Bean提供了基于SpEL表达式的权限校验模板，可用于自定义组合注解。</p>
     *
     * <h3>典型用法</h3>
     * <p>通过此配置可以简化自定义权限注解的定义，例如：</p>
     * <pre>{@code
     * &#064;Target({ElementType.METHOD, ElementType.TYPE})
     * &#064;Retention(RetentionPolicy.RUNTIME)
     * &#064;PreAuthorize("hasAnyAuthority(  // 使用模板提供的表达式语法
     * public @interface HasAnyAuthority {
     *     String[] auth();  // 接收权限列表参数
     * }
     * }</pre>
     *
     * <h3>注意事项</h3>
     * <ul>
     *   <li>需要确保Spring Security的预授权功能已启用</li>
     *   <li>模板表达式应符合SpEL语法规范</li>
     * </ul>
     *
     * @return PrePostTemplateDefaults 实例，用于预/后授权注解的默认配置
     * @see org.springframework.security.access.prepost.PreAuthorize
     * @see org.springframework.security.access.prepost.PostAuthorize
     */
    @Bean
    PrePostTemplateDefaults prePostTemplateDefaults() {
        return new PrePostTemplateDefaults();
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
