package example.security.authentication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PasswordEncoderTest {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void testPasswordEncoding() {
		String rawPassword = "password";
		String encodedPassword = passwordEncoder.encode(rawPassword);
		System.out.println("Encoded password: " + encodedPassword);

		// 应该返回 true
		boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
		System.out.println("Password matches: " + matches);

		assertTrue(matches);
	}
}