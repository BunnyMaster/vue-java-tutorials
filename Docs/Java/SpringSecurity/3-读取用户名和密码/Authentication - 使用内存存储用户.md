> [!TIP]
>
> 详细参考模块：`spring-serurity-7/authentication-memory`

## 使用内存存储用户

`InMemoryUserDetailsManager` 用于提供基于用户名/密码的认证支持，该认证信息存储在内存中。`InMemoryUserDetailsManager` 通过实现 `UserDetailsManager` 接口来管理 `UserDetails`。当 Spring Security 配置为接受用户名和密码进行认证时，就会使用基于 `UserDetails` 的认证方式。

如果要在内存中存储一个临时用户可以使用`User.builder()`方式，这种方式相对来说比较安全。

```java
@Bean
public UserDetailsService users() {
    UserDetails user = User.builder()
            .username("user")
            .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
            .roles("USER")
            .build();
    UserDetails admin = User.builder()
            .username("admin")
            .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
            .roles("USER", "ADMIN")
            .build();
    return new InMemoryUserDetailsManager(user, admin);
}
```

这种方式可以实现需求，但是极其不推荐，Spring Security 中已经将这个方法标识为“废弃”。如果使用这种方法很有可能会通过反编译的方式获取密码！

**完整的配置方式**

```java
/**
 * Security 配置
 *
 * @author bunny
 */
@Configuration
@EnableWebSecurity
public class AuthenticationConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 对于 Api 请求需要禁用 CSRF 保护
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/api/user/login").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        // 内存用户 1
        DaoAuthenticationProvider authenticationProvider1 = new DaoAuthenticationProvider(
                new InMemoryUserDetailsManager(User.builder()
                        .username("user_1")
                        .password(passwordEncoder.encode("password"))
                        .roles("USER")
                        .build())

        );
        authenticationProvider1.setPasswordEncoder(passwordEncoder);

        // 内存用户 2
        DaoAuthenticationProvider authenticationProvider2 = new DaoAuthenticationProvider(
                new InMemoryUserDetailsManager(User.builder()
                        .username("user_2")
                        .password(passwordEncoder.encode("password"))
                        .roles("USER")
                        .build())

        );
        authenticationProvider2.setPasswordEncoder(passwordEncoder);

        ProviderManager providerManager = new ProviderManager(authenticationProvider1, authenticationProvider2);
        providerManager.setEraseCredentialsAfterAuthentication(false);

        return providerManager;
    }

    /**
     * 配置密码编码器。
     *
     * <p>默认使用 {@link PasswordEncoderFactories#createDelegatingPasswordEncoder()}，
     * 该工厂方法创建了一个委托编码器，其默认算法为 {@link BCryptPasswordEncoder}，
     * 同时支持多种其他编码格式（如 PBKDF2、Scrypt、SHA-256 等）。
     *
     * <p><strong>注意：</strong>使用委托编码器而非直接实例化 {@code BCryptPasswordEncoder}
     * 可以确保系统：
     * <ul>
     *   <li>支持多种密码格式共存，便于从旧系统迁移</li>
     *   <li>允许未来无缝升级到更强的哈希算法</li>
     *   <li>保持密码验证的向后兼容性</li>
     * </ul>
     *
     * <p>只有在明确只需要 BCrypt 且无需考虑密码格式迁移的场景下，
     * 才考虑直接使用 {@code new BCryptPasswordEncoder()}。
     *
     * @return 密码编码器实例
     * @see PasswordEncoderFactories#createDelegatingPasswordEncoder()
     * @see DelegatingPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
```

