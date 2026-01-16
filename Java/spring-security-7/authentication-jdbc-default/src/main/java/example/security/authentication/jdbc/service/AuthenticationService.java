package example.security.authentication.jdbc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import example.security.authentication.jdbc.model.LoginRequest;
import example.security.authentication.jdbc.model.LoginResponse;
import example.security.authentication.jdbc.model.UserDetailsEntity;

/**
 * 身价验证服务，使用 api 访问方式
 *
 * @author bunny
 */
public interface AuthenticationService extends IService<UserDetailsEntity> {
	/**
	 * 用户登录
	 *
	 * @param request 登录请求
	 * @return 登录响应
	 */
	LoginResponse login(LoginRequest request);

	/**
	 * 用户注销
	 */
	void logout();
}
