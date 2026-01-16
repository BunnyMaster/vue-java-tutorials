package example.security.authentication.memory;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 身价验证-内存方式 启动类
 *
 * @author bunny
 */
@SpringBootApplication
public class AuthenticationMemoryMainApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthenticationMemoryMainApplication.class, args);
	}
}
