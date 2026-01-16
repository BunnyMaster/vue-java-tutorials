两个前置类：`AuthenticationManager`、`ProviderManager`。其中 `ProviderManager` 是 `AuthenticationManager` 的实现。

![ProviderManager](https://docs.spring.io/spring-security/reference/_images/servlet/authentication/architecture/providermanager.png)

**`AuthenticationManager`：**

```java
@FunctionalInterface
public interface AuthenticationManager {
    Authentication authenticate(Authentication authentication) throws AuthenticationException;
}
```

- 作用：
  - **认证请求的总入口**：负责处理认证请求
  - **策略模式接口**：定义了认证的标准接口

- 职责：
  - 接收一个未认证的 `Authentication` 对象（包含用户名/密码等凭证）
  - 验证凭证的有效性
  - 返回一个完全认证的 `Authentication` 对象（包含权限信息）
  - 抛出相应的认证异常

`ProviderManager`：

- 作用：
  - **`AuthenticationManager` 的主要实现**
  - **责任链模式**：委托给多个 `AuthenticationProvider` 进行认证
  - **认证策略协调器**：管理认证流程

同样多个`ProviderManager`可能会共用一个`AuthenticationManager`。

![](https://docs.spring.io/spring-security/reference/_images/servlet/authentication/architecture/providermanager-parent.png)

![](https://docs.spring.io/spring-security/reference/_images/servlet/authentication/architecture/providermanagers-parent.png)

## `AuthenticationProvider`

源码如下：

```java
public interface AuthenticationProvider {
	Authentication authenticate(Authentication authentication) throws AuthenticationException;
	boolean supports(Class<?> authentication);
}
```

可以发现其中的实现和 `AuthenticationManager` 只是多了一个 `supports` 方法。如果要实现一些自定义内容，可以实现 `AuthenticationProvider` 比如 JWT 认证。

在99%情况下，我们应当实现 `AuthenticationProvider`而不是`AuthenticationManager`因为 `ProviderManager` 已经提供了完善的认证调度逻辑。实现 `AuthenticationProvider` 后会将其加载到 `ProviderManager` 中，从如下的 `ProviderManager` 中可以看到，将 `List<AuthenticationProvider> providers`  加载到 `ProviderManager` 实例中。

```java
public class ProviderManager implements AuthenticationManager, MessageSourceAware, InitializingBean {
    // ...
    private List<AuthenticationProvider> providers = Collections.emptyList();
 
    // 将 List<AuthenticationProvider> providers 加载到 ProviderManager 实例中
	public ProviderManager(List<AuthenticationProvider> providers, AuthenticationManager parent) {
		Assert.notNull(providers, "providers list cannot be null");
		this.providers = providers;
		this.parent = parent;
		checkState();
	}
}
```

## `AbstractAuthenticationProcessingFilter`

AbstractAuthenticationProcessingFilter 是 Spring Security 处理用户凭证认证的过滤器基类，它定义了从请求提取认证信息、调用认证管理器、处理认证成功/失败的标准流程，是各种具体认证过滤器（如表单登录、OAuth2等）的模板实现。

![AbstractAuthenticationProcessingFilter](https://docs.spring.io/spring-security/reference/_images/servlet/authentication/architecture/abstractauthenticationprocessingfilter.png)

#### **第一步：创建认证对象**

- 从 `HttpServletRequest` 中提取用户凭证
- 创建对应的 `Authentication` 对象（具体类型由子类决定）
- 例如：`UsernamePasswordAuthenticationFilter` 创建 `UsernamePasswordAuthenticationToken`

#### **第二步：执行认证**

- 将认证对象传递给 `AuthenticationManager` 进行认证
- `AuthenticationManager` 协调多个 `AuthenticationProvider` 完成实际认证

#### **第三步：处理认证结果**

**认证失败处理：**

1. **清理安全上下文**：`SecurityContextHolder.clearContext()`
2. **RememberMe 失败处理**：`RememberMeServices.loginFail()`
3. **调用失败处理器**：`AuthenticationFailureHandler.onAuthenticationFailure()`
4. **会话策略通知**：`SessionAuthenticationStrategy` 收到新登录通知

**认证成功处理：**

1. **会话策略通知**：`SessionAuthenticationStrategy` 收到新登录通知
2. **合并权限**：已存在的认证权限合并到新认证对象中
3. **设置安全上下文**：`SecurityContextHolder.setContext()`
4. **保存安全上下文**：`SecurityContextRepository.saveContext()`（用于后续请求）
5. **RememberMe 成功处理**：`RememberMeServices.loginSuccess()`
6. **发布成功事件**：`ApplicationEventPublisher.publishEvent()`
7. **调用成功处理器**：`AuthenticationSuccessHandler.onAuthenticationSuccess()`