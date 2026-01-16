## 使用 JDBC 方式存储用户

> [!TIP]
>
> 详细参考模块：`spring-serurity-7/authentication-jdbc`

### 简介

默认的创建语句在`org/springframework/security/core/userdetails/jdbc/users.ddl`。但是在在这个模块中使用了自己的实现方式

`JdbcUserDetailsManager` 继承自 `JdbcDaoImpl`，通过 `UserDetailsManager` 接口来实现对用户详情的管理。当 Spring Security 配置为接受用户名/密码进行身份验证时，会使用基于用户详情的认证方式。

### 实现方式

1. 使用 H2 数据库——内存方式（如果项目重启数据会丢失）
2. `CustomUserDetailsService` 继承了 `IService<UserDetailsEntity>, UserDetailsService`

## 默认方式服务实现

### application配置

```yaml
server:
  port: 8802

spring:
  application:
    name: authentication-service
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:auth_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL
    username: sa
    password:
    hikari:
      connection-timeout: 30000
      maximum-pool-size: 20
      minimum-idle: 10
  sql:
    init:
      mode: always
      # 移除默认的 Spring Security DDL，使用自己的初始化脚本
      # schema-locations: classpath:org/springframework/security/core/userdetails/jdbc/users.ddl
      schema-locations: classpath:schema.sql
      data-locations: classpath:data.sql
  h2:
    console:
      enabled: true
      path: /h2-console
      settings:
        web-allow-others: false

# MyBatis Plus 配置
mybatis-plus:
  configuration:
    # 下划线转驼峰
    map-underscore-to-camel-case: true
    # 打印 SQL 日志（开发环境使用）
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      # 主键策略，数据库自增
      id-type: auto
  # XML 映射文件位置
  mapper-locations: classpath:mapper/*.xml

# 日志配置
logging:
  level:
    com.example.security: debug
    org.springframework.security: debug
```

### Spring Security 配置

```java
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
				// 表单登录禁用（API使用JSON登录）
				.formLogin(AbstractHttpConfigurer::disable)
				// 使用无状态Session（适合API）
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				// 配置请求路径的授权
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers("/api/public/**").permitAll()
						.requestMatchers("/h2-console/**", "/api/user/login", "/favicon.ico").permitAll()
						.anyRequest().authenticated()
				)
				// 配置H2控制台的frame选项
				.headers(headers -> headers
						// 使用默认值 .frameOptions(Customizer.withDefaults())
						// 完全禁用frame保护 .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
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

	/**
	 * 获取认证管理器。使用默认的 AuthenticationManager
	 *
	 * @param authConfig 认证配置
	 * @return 认证管理器实例
	 * @throws Exception 认证配置异常
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
}
```

### 用户实现类

```java
@Service
public class FormUserDetailsServiceImpl extends ServiceImpl<UserMapper, UserDetailsEntity> implements FormUserDetailsService {

	private final UserMapper userMapper;

	public FormUserDetailsServiceImpl(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	/**
	 * 根据用户名查询用户详情
	 *
	 * @param username 用户名
	 * @return 用户详情
	 * @throws UsernameNotFoundException 用户不存在
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 使用自定义查询
		UserDetailsEntity user = userMapper.selectUserWithAuthorities(username);

		// 在正式开发不要写 xxx 用户不存在。如果不存在请写：用户名或密码错误！减少安全问题
		if (user == null) {
			throw new UsernameNotFoundException("用户不存在: " + username);
		}

		return user;
	}
}
```