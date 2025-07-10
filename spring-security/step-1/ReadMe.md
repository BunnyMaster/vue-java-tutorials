# 入门案例

## SpringSecurity6基本使用

添加项目依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

创建一个类，加上下面两个注解即可`@EnableWebSecurity`,`@Configuration`

```java
@EnableWebSecurity
@Configuration
public class SecurityWebConfiguration {
}
```

## 自定义登录页

> [!IMPORTANT]
>
> 使用自定义页面时候，需要在控制器中指定当前跳转的地址，否则Security无法知道你要去往那个页面，即使写上了URL也无法跳转。

在下面示例中定义了自定义登录页，当然也可以定义错误页、退出页等等。

### 开启和禁用

如果需要使用默认的选项可以使用`.formLogin(Customizer.withDefaults())`即可。

如果需要禁用登录页`.formLogin(AbstractHttpConfigurer::disable)`。

### 需要认证指定URL地址

#### 普通认证拦截方式

需要认证URL地址，可以像下面这样写。

```java
String[] permitAllUrls = {
        "/", "/doc.html/**",
        "/webjars/**", "/images/**", ".well-known/**", "favicon.ico", "/error/**",
        "/v3/api-docs/**"
};

http.authorizeHttpRequests(authorizeRequests ->
                // 访问路径为 /api/** 时需要进行认证
                authorizeRequests
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers(permitAllUrls).permitAll()
        )
```

### 完整示例

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
                        // 访问路径为 /api/** 时需要进行认证
                        authorizeRequests
                                .requestMatchers("/api/**").authenticated()
                                .requestMatchers(permitAllUrls).permitAll()
                )
                .formLogin(loginPage -> loginPage
                        // 自定义登录页路径
                        .loginPage("/login-page")
                        // 处理登录的URL（默认就是/login）
                        .loginProcessingUrl("/login")
                        // 登录成功跳转
                        .defaultSuccessUrl("/")
                        // 登录失败跳转
                        .failureUrl("/login-page?error=true")
                        .permitAll()
                )
                // 使用默认的登录
                // .formLogin(Customizer.withDefaults())
                // 禁用表单登录
                // .formLogin(AbstractHttpConfigurer::disable)
                .logout(logout -> logout
                        .logoutSuccessUrl("/login-page?logout=true")
                        .permitAll()
                );
        return http.build();
    }

}
```