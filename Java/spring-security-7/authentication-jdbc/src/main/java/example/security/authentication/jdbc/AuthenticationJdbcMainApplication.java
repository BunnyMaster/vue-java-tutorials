package example.security.authentication.jdbc;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 身价验证-JDBC 启动类
 *
 * @author bunny
 */
@SpringBootApplication
public class AuthenticationJdbcMainApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthenticationJdbcMainApplication.class, args);
	}
}
