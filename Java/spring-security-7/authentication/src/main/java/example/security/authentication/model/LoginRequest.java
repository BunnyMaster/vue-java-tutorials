package example.security.authentication.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 登录请求
 *
 * @author bunny
 */
@Data
@AllArgsConstructor
public class LoginRequest {

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;
}