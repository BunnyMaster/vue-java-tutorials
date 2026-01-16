package example.security.architecture.config;

import example.security.architecture.filter.LogoutFilterAfter1;
import example.security.architecture.filter.LogoutFilterAfter2;
import example.security.architecture.filter.LogoutFilterAfter3;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(ArchitectureSecurityConfig.class)
class ArchitectureSecurityConfigIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * 如果需要可以 Mock 过滤器
	 */
	@MockitoBean
	private LogoutFilterAfter1 logoutFilterAfter1;

	@MockitoBean
	private LogoutFilterAfter2 logoutFilterAfter2;

	@MockitoBean
	private LogoutFilterAfter3 logoutFilterAfter3;

	@Test
	void shouldReturnFilterList() throws Exception {
		// Act & Assert
		MvcResult result = mockMvc.perform(get("/api/filter/list"))
				.andExpect(status().isOk())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		System.out.println("Filter list response: " + content);
	}

	@Test
	void shouldAllowUnauthenticatedAccess() throws Exception {
		// 测试未认证的请求
		mockMvc.perform(get("/api/filter/list"))
				.andExpect(status().isOk());
	}
}