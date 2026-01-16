package example.security.authentication.jdbc.service;


import example.security.authentication.jdbc.config.AuthenticationJdbcSecurityConfig;
import example.security.authentication.jdbc.model.LoginRequest;
import example.security.authentication.jdbc.model.LoginResponse;
import example.security.authentication.jdbc.model.UserDetailsEntity;
import example.security.authentication.jdbc.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Import({AuthenticationJdbcSecurityConfig.class,})
public class AuthenticationServiceTest {

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private Authentication authentication;

	@InjectMocks
	private AuthenticationServiceImpl authenticationService;

	@Test
	public void testLoginSuccess() {
		// 模拟登录成功
		LoginRequest request = new LoginRequest("user", "password");

		// 创建UserDetailsEntity的mock对象
		UserDetailsEntity mockUserDetails = mock(UserDetailsEntity.class);
		when(mockUserDetails.getUsername()).thenReturn("user");
		when(mockUserDetails.getEmail()).thenReturn("user@example.com");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
		when(authentication.getPrincipal()).thenReturn(mockUserDetails);

		LoginResponse response = authenticationService.login(request);

		assertTrue(response.isSuccess());
		assertEquals("登录成功", response.getMessage());
		assertNotNull(response.getToken());
	}


	@Test
	public void testLoginFailure() {
		// 模拟登录失败
		LoginRequest request = new LoginRequest("user", "wrong");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenThrow(new BadCredentialsException("认证失败"));

		LoginResponse response = authenticationService.login(request);

		assertFalse(response.isSuccess());
		assertEquals("用户名或密码错误", response.getMessage());
	}

}