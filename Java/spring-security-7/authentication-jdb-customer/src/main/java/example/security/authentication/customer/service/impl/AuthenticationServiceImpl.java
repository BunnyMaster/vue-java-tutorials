package example.security.authentication.customer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.security.authentication.customer.mapper.UserMapper;
import example.security.authentication.customer.model.LoginRequest;
import example.security.authentication.customer.model.LoginResponse;
import example.security.authentication.customer.model.UserDetailsEntity;
import example.security.authentication.customer.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * 身价验证服务，使用 api 访问方式
 *
 * @author bunny
 */
@Service
public class AuthenticationServiceImpl extends ServiceImpl<UserMapper, UserDetailsEntity> implements AuthenticationService {

	private final AuthenticationManager authenticationManager;

	public AuthenticationServiceImpl(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	/**
	 * 用户登录
	 *
	 * @param request 登录请求
	 * @return 登录响应
	 */
	@Override
	public LoginResponse login(LoginRequest request) {
		try {
			// TODO 修改成登录方式 LoginTypeEnum
			// 使用Spring Security进行认证
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							request.getUsername(),
							request.getPassword()
					)
			);

			// 设置认证信息到SecurityContext
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// 获取用户详情
			UserDetailsEntity userDetails = (UserDetailsEntity) authentication.getPrincipal();

			// 生成JWT token（如果需要）
			String token = generateToken(userDetails);

			// 返回登录成功响应
			return LoginResponse.success(
					token,
					userDetails.getUsername(),
					userDetails.getEmail()
			);

		} catch (AuthenticationException e) {
			return LoginResponse.failure("用户名或密码错误");
		}
	}

	/**
	 * 用户注销
	 */
	@Override
	public void logout() {
		// 比如清除 Redis 缓存等
		SecurityContextHolder.clearContext();
	}

	/**
	 * 生成JWT token（示例，实际使用需要JWT依赖）
	 */
	private String generateToken(UserDetails userDetails) {
		// 这里可以集成JWT token生成，暂时返回一个简单的token
		return "example-token-" + userDetails.getUsername() + "-" + System.currentTimeMillis();
	}
}