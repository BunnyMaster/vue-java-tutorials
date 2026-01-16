### 核心接口与类

![](https://docs.spring.io/spring-security/reference/_images/servlet/authentication/architecture/providermanagers-parent.png)

- `UserDetailsService`：加载用户数据
- `PasswordEncoder`：密码编码器
- `AuthenticationManager`：认证管理器
  - `ProviderManager`
  - `AuthenticationProvider`
- `SecurityContextHolder`：安全上下文持有者
- `UsernamePasswordAuthenticationFilter`：表单登录过滤器
- `BasicAuthenticationFilter`：Basic认证过滤器
- `CsrfFilter`：CSRF 防护过滤器
- `CorsFilter`：跨域过滤器
- `ExceptionTranslationFilter`：异常处理过滤器