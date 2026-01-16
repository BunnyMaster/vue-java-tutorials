package example.security.authentication.memory.controller;

import com.alibaba.fastjson2.JSON;
import example.security.authentication.memory.config.AuthenticationConfig;
import example.security.authentication.memory.model.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import({AuthenticationConfig.class,})
class LoginControllerTest {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * 测试内存用户 1
	 *
	 * @throws Exception Exception
	 */
	@Test
	void loginSuccess_withUser1() throws Exception {
		LoginRequest loginRequest = new LoginRequest("user_1", "password");

		mockMvc.perform(post("/api/user/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(JSON.toJSONString(loginRequest)))
				.andExpect(status().isOk());
	}

	/**
	 * 测试内存用户 2
	 *
	 * @throws Exception Exception
	 */
	@Test
	void loginSuccess_withUser2() throws Exception {
		LoginRequest loginRequest = new LoginRequest("user_2", "password");

		mockMvc.perform(post("/api/user/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(JSON.toJSONString(loginRequest)))
				.andExpect(status().isOk());
	}

	/**
	 * 测试用户不存在
	 *
	 * @throws Exception Exception
	 */
	@Test
	void loginFailure_withWrongPassword() throws Exception {
		LoginRequest loginRequest = new LoginRequest("xxx", "wrong_password");

		mockMvc.perform(post("/api/user/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(JSON.toJSONString(loginRequest)))
				.andExpect(status().isForbidden());
	}
}