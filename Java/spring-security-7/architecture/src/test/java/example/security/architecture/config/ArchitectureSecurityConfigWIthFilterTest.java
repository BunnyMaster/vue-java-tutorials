package example.security.architecture.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = ArchitectureSecurityConfig.class)
class ArchitectureSecurityConfigWIthFilterTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
		// 模拟用户登录
	void testFilterOrder() throws Exception {
		// 发送请求触发过滤器链
		mockMvc.perform(get("/api/filter/list"))
				.andDo(print())  // 打印请求响应信息
				.andExpect(status().isOk());
	}

	@Test
	void testLogoutTrigger() throws Exception {
		// 测试登出请求，触发LogoutFilter
		mockMvc.perform(get("/logout"))
				.andDo(print())
				.andExpect(status().is3xxRedirection());
	}
}