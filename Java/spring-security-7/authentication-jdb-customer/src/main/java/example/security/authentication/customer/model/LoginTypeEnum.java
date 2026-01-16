package example.security.authentication.customer.model;

import lombok.Getter;

/**
 * 登录类型枚举
 *
 * @author bunny
 */
@Getter
public enum LoginTypeEnum {
	/**
	 * 用户名密码登录
	 */
	USERNAME_PASSWORD("username_password"),

	/**
	 * 邮箱密码登录
	 */
	EMAIL_PASSWORD("email_password"),

	/**
	 * 手机密码登录
	 */
	PHONE_PASSWORD("phone_password"),
	;

	private final String type;

	LoginTypeEnum(String type) {
		this.type = type;
	}
}
