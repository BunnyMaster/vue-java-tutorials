# Spring Security 6 入门指南优化版

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

## 完整配置示例

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
