package example.security.authentication.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应
 *
 * @author bunny
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
	/**
	 * JWT token（如果使用JWT）
	 */
	private String token;

	/**
	 * 令牌类型
	 */
	@Builder.Default
	private String type = "Bearer";

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 登录信息
	 */
	private String message;

	/**
	 * 是否成功
	 */
	private boolean success;

	public static LoginResponse success(String token, String username, String email) {
		return LoginResponse.builder()
				.success(true)
				.message("登录成功")
				.token(token)
				.username(username)
				.email(email)
				.build();
	}

	public static LoginResponse failure(String message) {
		return LoginResponse.builder()
				.success(false)
				.message(message)
				.build();
	}
}