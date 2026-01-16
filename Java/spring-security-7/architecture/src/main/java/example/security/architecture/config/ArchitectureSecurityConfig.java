package example.security.architecture.config;

import example.security.architecture.filter.LogoutFilterAfter1;
import example.security.architecture.filter.LogoutFilterAfter2;
import example.security.architecture.filter.LogoutFilterAfter3;
import example.security.architecture.filter.TenantFilter;
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
 * @author bunny
 */
@Configuration
@EnableWebSecurity
public class ArchitectureSecurityConfig {

	private final LogoutFilterAfter1 logoutFilterAfter1;
	private final LogoutFilterAfter2 logoutFilterAfter2;
	private final LogoutFilterAfter3 logoutFilterAfter3;

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

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.httpBasic(Customizer.withDefaults())
				// 使用 Spring 管理的 Bean 实例，确保顺序和单例
				.addFilterAfter(new TenantFilter(), AnonymousAuthenticationFilter.class)
				.addFilterAfter(logoutFilterAfter1, LogoutFilter.class)
				.addFilterAfter(logoutFilterAfter2, LogoutFilter.class)
				.addFilterAfter(logoutFilterAfter3, LogoutFilter.class)
		;

		return http.build();
	}
}
