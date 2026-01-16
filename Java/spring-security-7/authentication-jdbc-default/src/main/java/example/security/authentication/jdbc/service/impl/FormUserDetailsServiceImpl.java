package example.security.authentication.jdbc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.security.authentication.jdbc.mapper.UserMapper;
import example.security.authentication.jdbc.model.UserDetailsEntity;
import example.security.authentication.jdbc.service.FormUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现，用于表单登录
 *
 * @author bunny
 */
@Service
public class FormUserDetailsServiceImpl extends ServiceImpl<UserMapper, UserDetailsEntity> implements FormUserDetailsService {

	private final UserMapper userMapper;

	public FormUserDetailsServiceImpl(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	/**
	 * 根据用户名查询用户详情
	 *
	 * @param username 用户名
	 * @return 用户详情
	 * @throws UsernameNotFoundException 用户不存在
	 */
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