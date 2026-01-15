## 声明过滤器 Bean

### 非常规、错误示例

**重复使用**

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

**顺序错误**

下面出现的问题是因为没有正确使用，但是可以实现部分效果，不要模仿！

1. 如果当前的 Filter 没有使用 `@Component` 并且使用 new 的方式
   - 会在控制台中有一次日志输出。
   - 如果这时加上了 `@Order(123)` 并不会按照指定顺序输出。
2.  如果这时加上了 `@Component` 并且在使用时用 new 方式   
   - 这时控制台会有两次输出。
   - 如果加上了 `@Order(123)` 在 输出时某次的输出会按照 `@Order` 指定的顺序输出 。
3. 如果这时 加上了 `@Component `使用构造函数方式进行使用会有一次输出，但是顺序并不是按照指定的顺序，而是书写的顺序。

```java
@Configuration
@EnableWebSecurity
public class ArchitectureSecurityConfig {

	private final LogoutFilterAfter1 logoutFilterAfter1;

	public ArchitectureSecurityConfig(LogoutFilterAfter1 logoutFilterAfter1) {
		this.logoutFilterAfter1 = logoutFilterAfter1;
	}

	/**
	 * 配置过滤器链
	 *
	 * @param http HttpSecurity
	 * @return SecurityFilterChain
	 * @throws Exception Exception
	 */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.httpBasic(Customizer.withDefaults())
				// .addFilterAt(new TenantFilter(), LogoutFilter.class)
				// 在 LogoutFilter 之后添加三个过滤器
				.addFilterAfter(logoutFilterAfter1, LogoutFilter.class)
				.addFilterAfter(new LogoutFilterAfter2(), LogoutFilter.class)
				.addFilterAfter(new LogoutFilterAfter3(), LogoutFilter.class)
		;

		return http.build();
	}
}

@Slf4j
@Order(0)
@Component
public class LogoutFilterAfter1 extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
									@NotNull FilterChain filterChain) throws ServletException, IOException {
		log.info("===>LogoutFilterAfter1");
		doFilter(request, response, filterChain);
	}
}

@Slf4j
@Order(100)
@Component
public class LogoutFilterAfter2 extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
       log.info("===>LogoutFilterAfter2");
       doFilter(request, response, filterChain);
    }
}

@Slf4j
public class LogoutFilterAfter3 extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
		log.info("===>LogoutFilterAfter3");
		doFilter(request, response, filterChain);
	}
}
```

### 原因说明

1. **重复注册**：通过 `@Component` 注解的 Filter 会被 Spring 自动注册，再加上手动 `addFilterAfter` 会导致重复注册
2. **顺序混乱**：`@Order` 注解只影响自动注册的 Filter 顺序，不影响手动添加的顺序
3. **双重实例**：new 创建的新实例与 Spring 管理的实例不是同一个

声明无论是用 `@Component`，还是在配置文件中声明为一个 bean Spring Boot 都会将其注入到 **`Servlet` 容器**。这可能会导致该过滤器被调用两次，一次是由容器调用，另一次是由 Spring Security 调用，并且调用的顺序可能不同。正因为如此，过滤器通常不是 Spring 框架中的 Bean。

然而，如果需要是一个 Spring bean 过滤器（例如为了利用依赖注入功能），可以通过声明一个 `FilterRegistrationBean` bean 并将其 enabled 属性设置为 false 来告知 Spring Boot 不要将它注册到容器中

### 推荐 Spring Bean 管理

在了解之前需要知道如下方式。下面的方式是 Spring Security 推荐的方式，如果当前实例需要使用到**依赖注入功能**，将**自动注册**设置为 `false`来告知 Spring Boot 不要将它注册到容器中。

```java
@Bean
public FilterRegistrationBean<LogoutFilterAfter1> disableAutoRegistration1(LogoutFilterAfter1 filter) {
    FilterRegistrationBean<LogoutFilterAfter1> registration = new FilterRegistrationBean<>(filter);
     // 禁用自动注册
    registration.setEnabled(false);
    return registration;
}
```

下面是正确的参考：

> [!IMPORTANT]
>
> 在 Spring Security 的 `addFilterAfter/addFilterBefore` 中，**执行顺序完全由代码书写顺序决定**，与 `@Order` 注解无关。

```java
@Configuration
@EnableWebSecurity
public class ArchitectureSecurityConfig {

    private final LogoutFilterAfter1 logoutFilterAfter1;
    private final LogoutFilterAfter2 logoutFilterAfter2;
    private final LogoutFilterAfter3 logoutFilterAfter3;

    // 通过构造函数注入所有 Filter
    public ArchitectureSecurityConfig(
            LogoutFilterAfter1 logoutFilterAfter1,
            LogoutFilterAfter2 logoutFilterAfter2,
            LogoutFilterAfter3 logoutFilterAfter3) {
        this.logoutFilterAfter1 = logoutFilterAfter1;
        this.logoutFilterAfter2 = logoutFilterAfter2;
        this.logoutFilterAfter3 = logoutFilterAfter3;
    }

    @Bean
    public FilterRegistrationBean<LogoutFilterAfter1> disableAutoRegistration1(LogoutFilterAfter1 filter) {
        FilterRegistrationBean<LogoutFilterAfter1> registration = new FilterRegistrationBean<>(filter);
         // 禁用自动注册
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<LogoutFilterAfter2> disableAutoRegistration2(LogoutFilterAfter2 filter) {
        FilterRegistrationBean<LogoutFilterAfter2> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<LogoutFilterAfter3> disableAutoRegistration3(LogoutFilterAfter3 filter) {
        FilterRegistrationBean<LogoutFilterAfter3> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic(Customizer.withDefaults())
            // 使用 Spring 管理的 Bean 实例，确保顺序和单例
            .addFilterAfter(logoutFilterAfter1, LogoutFilter.class)
            .addFilterAfter(logoutFilterAfter2, LogoutFilter.class)
            .addFilterAfter(logoutFilterAfter3, LogoutFilter.class);

        return http.build();
    }
}
```

