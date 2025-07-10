# Spring Security 6 入门指南

## 基本配置

### 添加依赖
在Maven项目中添加Spring Security依赖：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### 基础安全配置
创建一个配置类启用Web安全：
```java
@EnableWebSecurity
@Configuration
public class SecurityWebConfiguration {
}
```

## 自定义登录配置

### 重要提示
使用自定义页面时，必须在控制器中明确指定跳转地址，否则Security无法正确路由，即使URL正确也无法跳转。

### 启用与禁用选项
- 使用默认登录页：`.formLogin(Customizer.withDefaults())`
- 禁用表单登录：`.formLogin(AbstractHttpConfigurer::disable)`

## 认证与授权配置

### URL访问控制

#### 基本认证拦截
```java
String[] permitAllUrls = {
    "/", "/doc.html/**",
    "/webjars/**", "/images/**", ".well-known/**", "favicon.ico", "/error/**",
    "/v3/api-docs/**"
};

http.authorizeHttpRequests(authorizeRequests ->
    authorizeRequests
        .requestMatchers("/api/**").authenticated()
        .requestMatchers(permitAllUrls).permitAll()
)
```

#### 基于权限的拦截
> [!WARNING]
>
> 内存模式下无法获取角色信息。

1. 配置内存用户：
```java
@Bean
@ConditionalOnMissingBean(UserDetailsService.class)
InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder) {
    String encodedPassword = passwordEncoder.encode("123456");
    
    UserDetails user = User.builder()
        .username("user")
        .password(encodedPassword)
        .roles("USER")
        .authorities("read")
        .build();
        
    UserDetails admin = User.builder()
        .username("admin")
        .password(encodedPassword)
        .roles("ADMIN")
        .authorities("all", "read")
        .build();
        
    return new InMemoryUserDetailsManager(user, admin);
}
```

2. 配置资源权限：
```java
authorizeRequests
    .requestMatchers(permitAllUrls).permitAll()
    .requestMatchers("/api/security/**").permitAll()
    .requestMatchers(HttpMethod.GET, "/api/anonymous/**").anonymous()
    // 使用hasRole会自动添加ROLE_前缀
    // .requestMatchers("/api/**").hasRole("ADMIN")
    .requestMatchers("/api/**").hasAnyAuthority("all", "read")
```

### 完整配置示例

```java
@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityWebConfiguration {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String[] permitAllUrls = {
            "/", "/doc.html/**",
            "/webjars/**", "/images/**", ".well-known/**", "favicon.ico", "/error/**",
            "/v3/api-docs/**"
        };

        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers(permitAllUrls).permitAll()
                    .requestMatchers("/api/security/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/anonymous/**").anonymous()
                    .requestMatchers("/api/**").hasAnyAuthority("all", "read")
            )
            .formLogin(loginPage -> loginPage
                .loginPage("/login-page")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login-page?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login-page?logout=true")
                .permitAll()
            );
        return http.build();
    }
}
```

## 密码校验器

```java
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
    // 实际项目中只需返回一个密码编码器
    return new BCryptPasswordEncoder();
    
    // 其他编码器示例（根据需求选择一种）:
    // return new Argon2PasswordEncoder(16, 32, 1, 1 << 14, 2);
    // return new SCryptPasswordEncoder();
    // return new Pbkdf2PasswordEncoder("secret", 185000, 256);
}
```

### 密码校验器的作用和特点

#### 作用

密码校验器(PasswordEncoder)在Spring Security中负责：

1. 密码加密 - 将明文密码转换为不可逆的哈希值
2. 密码验证 - 比较输入的密码与存储的哈希是否匹配
3. 防止密码泄露 - 即使数据库泄露，攻击者也无法轻易获得原始密码

#### 各编码器特点

1. **BCryptPasswordEncoder**
   - 使用bcrypt算法
   - 自动加盐，防止彩虹表攻击
   - 可配置强度参数(默认10)
   - 目前最推荐的密码哈希方案
2. **Argon2PasswordEncoder**
   - 使用Argon2算法(2015年密码哈希比赛获胜者)
   - 抗GPU/ASIC攻击
   - 内存密集型，参数配置复杂
   - 适合高安全需求场景
3. **SCryptPasswordEncoder**
   - 使用scrypt算法
   - 内存密集型，抗硬件攻击
   - 比bcrypt更抗ASIC攻击
4. **Pbkdf2PasswordEncoder**
   - 使用PBKDF2算法
   - 较老的算法，但广泛支持
   - 需要高迭代次数才安全

### Spring Security中的最佳实践

**最佳实践是使用BCryptPasswordEncoder**，原因包括：

1. 它是Spring Security默认推荐的编码器
2. 自动处理盐值，无需额外存储
3. 经过充分的安全审查和实际验证
4. 平衡了安全性和性能
5. 广泛支持，易于配置

在Spring Security 5+版本中，BCryptPasswordEncoder是官方文档中首推的密码编码器实现。除非有特殊安全需求，否则应优先选择它。