package example.security.architecture.config;

import example.security.architecture.filter.LogoutFilterAfter1;
import example.security.architecture.filter.LogoutFilterAfter2;
import example.security.architecture.filter.LogoutFilterAfter3;
import example.security.architecture.filter.TenantFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * 配置篇章的过滤器示例，分为3个，每个里面都有日志输出，可以调整写入顺序，测试不同的顺序，查看日志输出
 * <p>
 * 在 Spring Security 的 <code>addFilterAfter/addFilterBefore</code> 中，<strong>执行顺序完全由代码书写顺序决定</strong>，与 <code>@Order</code> 注解无关。
 * </p>
 *
 * @author bunny
 */
@Configuration
@EnableWebSecurity
public class ArchitectureSecurityConfig {

	private final LogoutFilterAfter1 logoutFilterAfter1;
	private final LogoutFilterAfter2 logoutFilterAfter2;
	private final LogoutFilterAfter3 logoutFilterAfter3;

	public ArchitectureSecurityConfig(LogoutFilterAfter1 logoutFilterAfter1, LogoutFilterAfter2 logoutFilterAfter2, LogoutFilterAfter3 logoutFilterAfter3) {
		this.logoutFilterAfter1 = logoutFilterAfter1;
		this.logoutFilterAfter2 = logoutFilterAfter2;
		this.logoutFilterAfter3 = logoutFilterAfter3;
	}

	/**
	 * <p> 以下设置是为了在使用 依赖注入时，同时不会影响这些过滤器的执行顺序，如果没有禁用自动注册，会注入再次。</p>
	 * <p>无论是用 <code>@Component</code>，还是在配置文件中声明为一个 bean Spring Boot 都会将其注入到 Servlet 容器。
	 * 这可能会导致该过滤器被调用两次，一次是由容器调用，另一次是由 Spring Security 调用，并且调用的顺序可能不同。正因为如此，过滤器通常不是 Spring 框架中的 Bean。
	 * </p>
	 *
	 * @param filter LogoutFilterAfter1
	 * @return FilterRegistrationBean
	 */
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
		// 禁用自动注册
		registration.setEnabled(false);
		return registration;
	}

	@Bean
	public FilterRegistrationBean<LogoutFilterAfter3> disableAutoRegistration3(LogoutFilterAfter3 filter) {
		FilterRegistrationBean<LogoutFilterAfter3> registration = new FilterRegistrationBean<>(filter);
		// 禁用自动注册
		registration.setEnabled(false);
		return registration;
	}

	/**
	 * 配置过滤器链，<strong>执行顺序完全由代码书写顺序决定</strong>一共有三种配置方式：
	 * <ol>
	 * 		<li>{@link HttpSecurity#addFilterBefore(Filter, Class)}</li>
	 *  	<li>{@link HttpSecurity#addFilterAt(Filter, Class)}</li>
	 *  	<li>{@link HttpSecurity#addFilterAfter(Filter, Class)}</li>
	 * </ol>
	 * <p>
	 * 注意：{@link HttpSecurity#addFilter(Filter)} 是因为遗留代码或明确需要最后执行：
	 * 在 Spring Security 7 中如果使用 {@link HttpSecurity#addFilter(Filter)} 必需放在最后，否则会抛出异常
	 * </p>
	 * 早期版本中，addFilter 是主要的添加方式 Spring Security 5+ 引入了更精细的控制方法 但为了向后兼容保留了 addFilter
	 * </p>
	 *
	 * @param http HttpSecurity
	 * @return SecurityFilterChain
	 * @throws Exception Exception
	 */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http

				.httpBasic(Customizer.withDefaults())
				// 使用 Spring 管理的 Bean 实例，确保顺序和单例
				.addFilterAfter(new TenantFilter(), AnonymousAuthenticationFilter.class)
				.addFilterAfter(logoutFilterAfter1, LogoutFilter.class)
				.addFilterAfter(logoutFilterAfter2, LogoutFilter.class)
				.addFilterAfter(logoutFilterAfter3, LogoutFilter.class)
				.addFilter(new TenantFilter())
		;

		return http.build();
	}
}
