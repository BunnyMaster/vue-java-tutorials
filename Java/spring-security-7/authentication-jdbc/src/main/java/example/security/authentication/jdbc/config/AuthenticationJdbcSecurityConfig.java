package example.security.authentication.jdbc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 身价验证-JDBC 配置
 * <p>
 * 这个配置是使用 H2 数据库作为用户信息存储。密码加密器是使用{@link #passwordEncoder()}委托方式（默认的 BCrypt 算法）。
 * 可以访问 <a href="http://localhost:8080/h2-console/">登录数据库</a>。
 * </p>
 *
 * <p>
 * 放行请求参考 {@link #securityFilterChain} <code>.requestMatchers(xxx).permitAll()</code>
 * </p>
 *
 * <p>
 * 用户登录信息存储在数据库中，用户名是唯一的，用户名是唯一的，密码是加密存储的。
 * </p>
 *
 * <ul>
 *     <li>用户名：user 密码：password</li>
 *     <li>用户名：admin 密码：password</li>
 * </ul>
 *
 * @author bunny
 */
@Configuration
@EnableWebSecurity
public class AuthenticationJdbcSecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// 对于 Api 请求需要禁用 CSRF 保护
				.csrf(AbstractHttpConfigurer::disable)
				// 启用HTTP Basic认证（可选，用于H2控制台）
				.httpBasic(Customizer.withDefaults())
				// 配置请求路径的授权
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers("/api/public/**").permitAll()
						.requestMatchers("/h2-console/**", "/api/user/login", "/favicon.ico").permitAll()
						.anyRequest().authenticated()
				)
				// 配置H2控制台的frame选项
				.headers(headers -> headers
						// 使用默认值
						.frameOptions(Customizer.withDefaults())
						// 完全禁用frame保护
						// .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
						// 允许同源iframe
						.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
				)
		;

		return http.build();
	}

	/**
	 * 配置密码编码器。
	 *
	 * <p>默认使用 {@link PasswordEncoderFactories#createDelegatingPasswordEncoder()}，
	 * 该工厂方法创建了一个委托编码器，其默认算法为 {@link BCryptPasswordEncoder}，
	 * 同时支持多种其他编码格式（如 PBKDF2、Scrypt、SHA-256 等）。
	 *
	 * <p><strong>注意：</strong>使用委托编码器而非直接实例化 {@code BCryptPasswordEncoder}
	 * 可以确保系统：
	 * <ul>
	 *   <li>支持多种密码格式共存，便于从旧系统迁移</li>
	 *   <li>允许未来无缝升级到更强的哈希算法</li>
	 *   <li>保持密码验证的向后兼容性</li>
	 * </ul>
	 *
	 * <p>只有在明确只需要 BCrypt 且无需考虑密码格式迁移的场景下，
	 * 才考虑直接使用 {@code new BCryptPasswordEncoder()}。
	 *
	 * @return 密码编码器实例
	 * @see PasswordEncoderFactories#createDelegatingPasswordEncoder()
	 * @see DelegatingPasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
