package example.security.authentication.jdbc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.security.authentication.jdbc.mapper.UserMapper;
import example.security.authentication.jdbc.model.UserDetailsEntity;
import example.security.authentication.jdbc.service.CustomUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author bunny
 */
@Service
public class CustomUserDetailsServiceImpl extends ServiceImpl<UserMapper, UserDetailsEntity> implements UserDetailsService, CustomUserDetailsService {

	private final UserMapper userMapper;

	public CustomUserDetailsServiceImpl(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 使用自定义查询
		UserDetailsEntity user = userMapper.selectUserWithAuthorities(username);

		// 在正式开发不要写 xxx 用户不存在。如果不存在请写：用户名或密码错误！减少安全问题
		if (user == null) {
			throw new UsernameNotFoundException("用户不存在: " + username);
		}

		return user;
	}
}