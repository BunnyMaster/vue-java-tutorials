## 核心接口与类

![](https://docs.spring.io/spring-security/reference/_images/servlet/authentication/architecture/providermanagers-parent.png)

要明白下面几个类分作的作用是什么

- `UserDetailsService`：加载用户数据
  - 一般用于表单请求
- `PasswordEncoder`：密码编码器
  - 从 Spring Security 5 开始推荐使用**委托代理密码器**
- `AuthenticationManager`：认证管理器（正常不用实现这个）
  - `ProviderManager`：父类是 `AuthenticationManager`
  - `AuthenticationProvider`：如果有自定义需求可以实现这个类
- `SecurityContextHolder`：安全上下文
  - 如果要获取、设置调用里面方式，默认使用 `ThreadLocal` 方式（可以更改）
- `UsernamePasswordAuthenticationFilter`：表单登录过滤器
- `BasicAuthenticationFilter`：Basic认证过滤器
- `CsrfFilter`：CSRF 防护过滤器
- `CorsFilter`：跨域过滤器
- `ExceptionTranslationFilter`：异常处理过滤器
- `SecurityContextHolderFilter` ：攻击防护过滤器
- `LogoutFilter`：身份验证过滤器
- `AnonymousAuthenticationFilter`：授权过滤器

