package example.security.authentication.memory.controller;

import com.alibaba.fastjson2.JSON;
import example.security.authentication.memory.model.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 登录控制器
 *
 * @author bunny
 */
@Slf4j
@RequestMapping("/api/user")
@RestController
public class LoginController {

	private final AuthenticationManager authenticationManager;

	public LoginController(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/login")
	public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
		// 创建认证请求
		Authentication authenticationRequest = UsernamePasswordAuthenticationToken.authenticated(
				loginRequest.getUsername(),
				loginRequest.getPassword(),
				List.of(new SimpleGrantedAuthority("USER"))
		);
		log.info("创建认证请求：{}", JSON.toJSONString(authenticationRequest));

		// 执行认证
		Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);

		log.info("认证信息：{}", JSON.toJSONString(authenticationResponse));

		// 如果认证成功，Spring Security 会自动处理，不会抛出异常
		return ResponseEntity.ok().build();
	}

}