## Security架构

### `HTTP Servlet`架构

这里`HTTP Servlet`请求一个典型架构：

![A Review of Filters](https://docs.spring.io/spring-security/reference/_images/servlet/architecture/filterchain.png)

以下是`Filter`接口源码，包位于`jakarta.servlet`

```java
package jakarta.servlet;

import java.io.IOException;

public interface Filter {
    default void init(FilterConfig filterConfig) throws ServletException {
    }

    void doFilter(ServletRequest var1, ServletResponse var2, FilterChain var3) throws IOException, ServletException;

    default void destroy() {
    }
}
```

### `DelegatingFilterProxy`

`DelegatingFilterProxy`是`SpringWeb`中的一个==委托代理，在`SpringWeb`中，并非`SpringSecurity`。==

```java
public class DelegatingFilterProxy extends GenericFilterBean {}
```

![DelegatingFilterProxy](https://docs.spring.io/spring-security/reference/_images/servlet/architecture/delegatingfilterproxy.png)

`DelegatingFilterProxy`可以==延迟加载==`FilterBean`这一点非常重要，因为容器需要在注册 Filter 实例之前先对其进行注册，而 Spring 通常使用 `ContextLoaderListener` 来加载 Spring Bean，而这一过程是在需要注册 `Filter` 实例之后才进行的。

**说明**

`DelegatingFilterProxy`意思是：等到Spring上下文所有初始化后（**所有 Bean 包括 Filter Bean 都加载完成**）的第一次请求时才会加载并委托给实际的Bean。

这样好处是：启动顺序解耦、依赖注入支持、生命周期管理、灵活配置。

### `FilterChainProxy`

一一个具体的类，通常来说我们不会去配置`FilterChainProxy`，除非真的要非常精细的权限控制。一般有下面场景：

1. 完全自定义过滤器执行流程
2. 需要非标准的请求匹配逻辑
3. 高性能网关/代理场景
4. 混合安全框架集成
5. 复杂的多租户架构
6. 需要深度性能优化或监控

![FilterChainProxy](https://docs.spring.io/spring-security/reference/_images/servlet/architecture/filterchainproxy.png)

源码是继承了`GenericFilterBean`

```java
public class FilterChainProxy extends GenericFilterBean {}
```

`FilterChainProxy` （一个具体的类）会注册在 `DelegatingFilterProxy` 中，并且 `SpringSecurity` 中所有实现 `SecurityFilterChain` 的过滤器，都会放到 `FilterChainProxy` 中进行执行。

`SecurityFilterChain`

是一个接口，源码内容如下：

```java
public interface SecurityFilterChain {
    boolean matches(HttpServletRequest request);
    List<Filter> getFilters();
}
```

![SecurityFilterChain](https://docs.spring.io/spring-security/reference/_images/servlet/architecture/securityfilterchain.png)

在 `SecurityFilterChain` 中的安全过滤器通常是 Bean 类型，但它们是通过 `FilterChainProxy` 而非 `DelegatingFilterProxy` 进行注册的。

`FilterChainProxy` 在注册方式上比直接与 `Servlet` 容器或 `DelegatingFilterProxy` 进行注册具有诸多优势。

首先，它为 Spring Security 的所有 `Servlet` 支持提供了基础。因此，如果要对 Spring Security 的 `Servlet` 支持进行故障排查，那么在 `FilterChainProxy` 中添加调试点是一个很好的开始点。

![*Multiple SecurityFilterChain*](https://docs.spring.io/spring-security/reference/_images/servlet/architecture/multi-securityfilterchain.png)

实现 `SecurityFilterChain` 的过滤器会有多个，但只会匹配第一个。比如按照上图中的过滤器， `/api/message` 会被`/api/**` 和 `/**` 匹配，但只匹配第一个 `/api/**` 的过滤器。
### Security Filters

>[!TIP]
>`FilterOrderRegistration` 位于  `org.springframework.security.config.annotation.web.builders.FilterOrderRegistration` 下。

安全过滤器是通过 `SecurityFilterChain` API 插入到 `FilterChainProxy` 中的。这些过滤器可用于多种不同的目的，例如漏洞防护、身份验证、授权等等。这些过滤器会按照特定的顺序执行，以确保它们能在正确的时间被调用，例如，执行身份验证的过滤器应该在执行授权的过滤器之前被调用。

通常情况下，无需了解 Spring Security 过滤器的执行顺序。然而，有时了解其顺序是有益的，如果您想了解这些顺序，可以查看 `FilterOrderRegistration` 代码。这些安全过滤器通常通过一个`HttpSecurity`实例来声明。以下是 `FilterOrderRegistration` 节选部分，可以发现过滤器注册时顺序都是以 `100` 累加的。

```java
@SuppressWarnings("serial")
final class FilterOrderRegistration {

	private static final int INITIAL_ORDER = 100;

	private static final int ORDER_STEP = 100;

	private final Map<String, Integer> filterToOrder = new HashMap<>();

	FilterOrderRegistration() {
		Step order = new Step(INITIAL_ORDER, ORDER_STEP);
		put(DisableEncodeUrlFilter.class, order.next());
		put(ForceEagerSessionCreationFilter.class, order.next());
		// ...
}
```

|如果你的过滤器是以下类型|请将它放置在以下过滤器之后|因为这些事件已经发生|
|---|---|---|
|**攻击防护过滤器**|`SecurityContextHolderFilter`|1|
|**身份验证过滤器**|`LogoutFilter`|1, 2|
|**授权过滤器**|`AnonymousAuthenticationFilter`|1, 2, 3|

[^1]: 安全上下文已建立（SecurityContext established）
[^2]: 登出已处理（Logout processed）
[^3]: 匿名身份已分配（Anonymous authentication assigned）

>[!TIP]
>通常情况下，应用程序会添加自定义认证机制。这意味着它们应置于 `LogoutFilter` 之后。

比如要判断一个用户是否有多租户权限可以用下面这样的方式：

```java
public class TenantFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
		// 租户Id
        String tenantId = request.getHeader("X-Tenant-Id"); (1)
        // 是否可以访问
        boolean hasAccess = isUserAllowed(tenantId); (2)
        // 可以访问交给下一个过滤器（调用链中的其余过滤器）
        if (hasAccess) {
            filterChain.doFilter(request, response); (3)
            return;
        }
        // 不能访问抛出异常
        throw new AccessDeniedException("Access denied"); (4)
    }

}
```

>[!NOTE]
>`OncePerRequestFilter` 是来自Spring Web 中的非 Spring Security 提供。

在 Spring Security 中可以继承 `OncePerRequestFilter` 无需使用 Filter。该类是仅在每次请求中执行一次的过滤器的基类，并提供了带有 `HttpServletRequest` 和 `HttpServletResponse` 参数的 `doFilterInternal` 方法。

一般来说，将其添加在匿名认证过滤器之后，即该链中最后的认证过滤器位置，具体方式如下：

```java
@Bean
SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
    	// ...
        // 使用 `HttpSecurity#addFilterAfter` 方法将租户过滤器添加到匿名身份验证过滤器之后。
        .addFilterAfter(new TenantFilter(), AnonymousAuthenticationFilter.class);
    return http.build();
}
```

## 声明过滤器Bean

### 使用Bean方式添加过滤器

声名无论是用 `@Component`，还是在配置文件中声明为一个 bean Spring Boot 都会将其注入到 **`Servlet` 容器**。这可能会导致该过滤器被调用两次，一次是由容器调用，另一次是由 Spring Security 调用，并且调用的顺序可能不同。正因为如此，过滤器通常不是 Spring 框架中的 Bean。然而，如果需要是一个 Spring bean 过滤器（例如为了利用依赖注入功能），您可以通过声明一个 `FilterRegistrationBean` bean 并将其 enabled 属性设置为 false 来告知 Spring Boot 不要将它注册到容器中：

```java
@Bean
public FilterRegistrationBean<TenantFilter> tenantFilterRegistration(TenantFilter filter) {
    FilterRegistrationBean<TenantFilter> registration = new FilterRegistrationBean<>(filter);
    registration.setEnabled(false);
    return registration;
}
```

这就使得只有 `HttpSecurity` 这个组件会添加它。

然而，如果您想要自行构建一个 Spring Security 过滤器，您可以通过 `DSL` 中的 `addFilterAt` 方法来进行指定，如下所示：

```java
@Bean
SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	BasicAuthenticationFilter basic = new BasicAuthenticationFilter();
	// ... configure

	http
		// ...
		.addFilterAt(basic, BasicAuthenticationFilter.class);

	return http.build();
}
```

### 错误示例

请注意，如果该过滤器已添加过，则 Spring Security 会抛出异常。例如，调用 `HttpSecurity#httpBasic` 会为您添加一个 `BasicAuthenticationFilter`。因此，以下这种安排是失败的，因为有两个调用都在尝试添加 `BasicAuthenticationFilter`：

```java
@Bean
SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	BasicAuthenticationFilter basic = new BasicAuthenticationFilter();
	// ... configure

	http
		.httpBasic(Customizer.withDefaults())
		// ... on no! BasicAuthenticationFilter is added twice!
		.addFilterAt(basic, BasicAuthenticationFilter.class);

	return http.build();
}
```

> [!TIP]
>
> 如果要配置 `BasicAuthenticationFilter` 可以将下面的默认的 Spring Security 过滤器禁用
>
> ```java
> .httpBasic((basic) -> basic.disable())
> ```

## 处理安全异常

`ExceptionTranslationFilter` 能够将`AccessDeniedException`和`AuthenticationException`这两种异常转换为 HTTP 响应。异常转换过滤器被插入到过滤器链代理中，并作为安全过滤器之一而存在。
以下图片展示了 `ExceptionTranslationFilter` 与其他组件之间的关系：

![Handling Security Exceptions](https://docs.spring.io/spring-security/reference/_images/servlet/architecture/exceptiontranslationfilter.png)

在上图中解释如下：

1. `ExceptionTranslationFilter` 会调用 `FilterChain.doFilter(request, response)` 其余部分。
2. 如果当前认证出现了问题，会启动身份验证流程
   - 安全上下文管理器已被清空
   - 会将 `HttpServletRequest` 保存下来，以便在身份验证成功后能够使用它来重现原始请求。
   - 认证入口点用于向客户端请求凭证。例如，它可能会重定向到登录页面或者发送一个“WWW-Authenticate”标头。
3. 否则，如果抛出的是“访问被拒绝异常”，那么就表示“访问被拒绝”。此时会调用“访问被拒绝处理程序”来处理访问被拒绝的情况。

如果应用程序没有抛出“访问被拒绝异常”或“认证异常”，那么“异常转换过滤器”就不会执行任何操作。

