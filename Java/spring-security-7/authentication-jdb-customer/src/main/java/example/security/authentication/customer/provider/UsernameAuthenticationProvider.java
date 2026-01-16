package example.security.authentication.customer.provider;

import example.security.authentication.customer.model.LoginRequest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * 用户名验证方式
 *
 * @author bunny
 */
@Service
public class UsernameAuthenticationProvider implements AuthenticationProvider {
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		boolean from = authentication.isAssignableFrom(LoginRequest.class);
		boolean equals = LoginRequest.class.equals(authentication);

		// TODO 修改成 LoginTypeEnum 方式类进行判断
		boolean from1 = UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);

		return from;
	}

}
