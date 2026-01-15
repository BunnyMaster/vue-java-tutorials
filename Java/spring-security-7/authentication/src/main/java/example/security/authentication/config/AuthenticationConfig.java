package example.security.authentication.config;

import example.security.authentication.storage.JdbcAuthentication;
import example.security.authentication.storage.MemoryAuthentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.util.List;

/**
 * Security 配置
 *
 * @author bunny
 */
@Configuration
@EnableWebSecurity
public class AuthenticationConfig {
    private final MemoryAuthentication memoryAuthentication;
    private final JdbcAuthentication jdbcAuthentication;

    public AuthenticationConfig(MemoryAuthentication memoryAuthentication, JdbcAuthentication jdbcAuthentication) {
        this.memoryAuthentication = memoryAuthentication;
        this.jdbcAuthentication = jdbcAuthentication;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/login").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);


        return new ProviderManager(authenticationProvider,
                new DaoAuthenticationProvider(memoryAuthentication.users()),
                new DaoAuthenticationProvider(memoryAuthentication.users2()),
                new DaoAuthenticationProvider(jdbcAuthentication.databaseUsers(dataSource()))
        );
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUserDetails(new User("bunny", "123456", List.of()))
                .build();
        return new InMemoryUserDetailsManager(user);
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
