## PasswordEncoder

> [!TIP] 
>
> 来自 Spring Security 文档：https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html

| 编码器         | 安全等级   | 内存硬度   | GPU/ASIC抗性 | 性能 | 推荐场景             |
| :------------- | :--------- | :--------- | :----------- | :--- | :------------------- |
| **BCrypt**     | 高         | 中等       | 中等         | 快   | 通用场景，默认选择   |
| **Argon2**     | 非常高     | 高         | 高           | 中等 | 高安全要求，现代应用 |
| **SCrypt**     | 高         | 高         | 高           | 慢   | 需要内存硬度的场景   |
| **PBKDF2**     | 中         | 无         | 低           | 快   | 兼容性要求，FIPS环境 |
| **Delegating** | 取决于组合 | 取决于组合 | 取决于组合   | 中等 | 迁移场景，多编码共存 |

在下面示例中使用的 `User#withDefaultPasswordEncoder`  被认为在生产中是不安全的！

这段配置会自动向 `SecurityFilterChain` 注册一个内存中的 `UserDetailsService`，将 `DaoAuthenticationProvider` 注册到默认的 `AuthenticationManager` 中，并启用表单登录和 HTTP Basic 认证。

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorize) -> authorize
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

}
```

