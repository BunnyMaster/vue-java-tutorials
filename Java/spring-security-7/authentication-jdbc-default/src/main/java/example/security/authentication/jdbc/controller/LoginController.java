package example.security.authentication.jdbc.controller;

import example.security.authentication.jdbc.model.LoginRequest;
import example.security.authentication.jdbc.model.LoginResponse;
import example.security.authentication.jdbc.service.impl.AuthenticationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录控制器
 *
 * @author bunny
 */
@RequestMapping("/api/user")
@RestController
public class LoginController {

	private final AuthenticationServiceImpl authenticationService;

	public LoginController(AuthenticationServiceImpl authenticationService) {
		this.authenticationService = authenticationService;
	}

	/**
	 * 用户登录
	 *
	 * @param request 登录请求
	 * @return 登录响应
	 */
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
		LoginResponse response = authenticationService.login(request);
		return ResponseEntity.ok(response);
	}

	/**
	 * 用户注销
	 */
	@PostMapping("/logout")
	public ResponseEntity<?> logout() {
		authenticationService.logout();
		return ResponseEntity.ok("注销成功");
	}
}
