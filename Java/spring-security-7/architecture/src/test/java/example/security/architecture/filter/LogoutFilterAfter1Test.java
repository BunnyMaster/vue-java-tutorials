package example.security.architecture.filter;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LogoutFilterAfter1Test {

	private LogoutFilterAfter1 filter;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private MockFilterChain filterChain;

	@BeforeEach
	void setUp() {
		filter = new LogoutFilterAfter1();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		filterChain = new MockFilterChain();
	}

	@Test
	void shouldProcessRequest() throws ServletException, IOException {
		// Act
		filter.doFilterInternal(request, response, filterChain);

		// Assert - 过滤器应该正常执行，不会抛出异常
		assertNotNull(response);
	}
}