package example.security.architecture;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class FilterOrderIntegrationTest {

	private static final String TENANT_ID = "X-Tenant-Id";
	@Autowired
	private MockMvc mockMvc;

	@Test
	void testFilterChainAndController() throws Exception {
		// 测试过滤器链和控制器
		mockMvc.perform(get("/api/filter/list").header(TENANT_ID, "Basic YWRtaW46YWRtaW4="))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0]").value("example.security.architecture.filter.LogoutFilterAfter1"))
				.andExpect(jsonPath("$[1]").value("example.security.architecture.filter.LogoutFilterAfter2"))
				.andExpect(jsonPath("$[2]").value("example.security.architecture.filter.LogoutFilterAfter3"))
				.andReturn();
	}
}