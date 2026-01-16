package example.security.authentication.jdbc;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 身价验证-JDBC-默认方式实现 启动类
 * <p>
 * 只包含默认提供的 表单登录、用户名密码 登录
 * </p>
 *
 * @author bunny
 */
@SpringBootApplication
public class AuthenticationJdbcMainApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthenticationJdbcMainApplication.class, args);
	}
}
