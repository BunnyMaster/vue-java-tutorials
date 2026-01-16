package example.security.authentication.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 身价验证-JDBC-自定义方式实现 启动类
 * <p>
 * 包含自定义登录方式、用户名、邮箱、手机号登录方式
 * </p>
 *
 * @author bunny
 */
@SpringBootApplication
public class AuthenticationCustomerMainApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthenticationCustomerMainApplication.class, args);
	}
}
