## PasswordEncoder

> [!TIP]
>
> 来自 Spring Security 文档：https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html

| 编码器            | 安全等级  | 内存硬度  | GPU/ASIC抗性 | 性能  | 推荐场景         |
| :------------- | :---- | :---- | :--------- | :-- | :----------- |
| **BCrypt**     | 高     | 中等    | 中等         | 快   | 通用场景，默认选择    |
| **Argon2**     | 非常高   | 高     | 高          | 中等  | 高安全要求，现代应用   |
| **SCrypt**     | 高     | 高     | 高          | 慢   | 需要内存硬度的场景    |
| **PBKDF2**     | 中     | 无     | 低          | 快   | 兼容性要求，FIPS环境 |
| **Delegating** | 取决于组合 | 取决于组合 | 取决于组合      | 中等  | 迁移场景，多编码共存   |

> [!WARNING]
>
> 在下面示例中使用的 `User#withDefaultPasswordEncoder`  被认为在生产中是不安全的！

这段配置会自动向 `SecurityFilterChain` 注册一个内存中的 `UserDetailsService`，将 `DaoAuthenticationProvider` 注册到默认的 `AuthenticationManager` 中，并启用表单登录和 HTTP Basic 认证。

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((authorize) -> authorize
				.anyRequest().authenticated()
			)
			.httpBasic(Customizer.withDefaults())
			.formLogin(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails userDetails = User.withDefaultPasswordEncoder()
			.username("user")
			.password("password")
			.roles("USER")
			.build();

		return new InMemoryUserDetailsManager(userDetails);
	}

}
```

## 使用内存存储用户

`InMemoryUserDetailsManager` 用于提供基于用户名/密码的认证支持，该认证信息存储在内存中。`InMemoryUserDetailsManager` 通过实现 `UserDetailsManager` 接口来管理 `UserDetails`。当 Spring Security 配置为接受用户名和密码进行认证时，就会使用基于 `UserDetails` 的认证方式。

如果要在内存中存储一个临时用户可以使用`User.builder()`方式，这种方式相对来说比较安全。

```java
@Bean
public UserDetailsService users() {
    UserDetails user = User.builder()
            .username("user")
            .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
            .roles("USER")
            .build();
    UserDetails admin = User.builder()
            .username("admin")
            .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
            .roles("USER", "ADMIN")
            .build();
    return new InMemoryUserDetailsManager(user, admin);
}
```

> [!WARNING]
>
> 这种方式可以实现需求，但是极其不推荐，Spring Security 中已经将这个方法标识为“废弃”。如果使用这种方法很有可能会通过反编译的方式获取密码！

```java
@Bean
public UserDetailsService users() {
	// The builder will ensure the passwords are encoded before saving in memory
	UserBuilder users = User.withDefaultPasswordEncoder();
	UserDetails user = users
		.username("user")
		.password("password")
		.roles("USER")
		.build();
	UserDetails admin = users
		.username("admin")
		.password("password")
		.roles("USER", "ADMIN")
		.build();
	return new InMemoryUserDetailsManager(user, admin);
}
```

## 使用 JDBC 方式存储用户

> [!NOTE]
>
> 默认的创建语句在`org/springframework/security/core/userdetails/jdbc/users.ddl`.

`JdbcUserDetailsManager` 继承自 `JdbcDaoImpl`，通过 `UserDetailsManager` 接口来实现对用户详情的管理。当 Spring Security 配置为接受用户名/密码进行身份验证时，会使用基于用户详情的认证方式。