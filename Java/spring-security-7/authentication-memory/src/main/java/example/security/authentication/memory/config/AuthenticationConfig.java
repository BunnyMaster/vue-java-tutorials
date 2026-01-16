package example.security.authentication.memory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security 配置
 *
 * @author bunny
 */
@Configuration
@EnableWebSecurity
public class AuthenticationConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// 对于 Api 请求需要禁用 CSRF 保护
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers("/api/user/login").permitAll()
						.anyRequest().authenticated()
				);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
		// 内存用户 1
		DaoAuthenticationProvider authenticationProvider1 = new DaoAuthenticationProvider(
				new InMemoryUserDetailsManager(User.builder()
						.username("user_1")
						.password(passwordEncoder.encode("password"))
						.roles("USER")
						.build())

		);
		authenticationProvider1.setPasswordEncoder(passwordEncoder);

		// 内存用户 2
		DaoAuthenticationProvider authenticationProvider2 = new DaoAuthenticationProvider(
				new InMemoryUserDetailsManager(User.builder()
						.username("user_2")
						.password(passwordEncoder.encode("password"))
						.roles("USER")
						.build())

		);
		authenticationProvider2.setPasswordEncoder(passwordEncoder);

		ProviderManager providerManager = new ProviderManager(authenticationProvider1, authenticationProvider2);
		providerManager.setEraseCredentialsAfterAuthentication(false);

		return providerManager;
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
