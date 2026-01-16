package example.security.authentication.jdbc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import example.security.authentication.jdbc.model.UserDetailsEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户映射器
 *
 * @author bunny
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDetailsEntity> {

	/**
	 * 查询用户信息
	 *
	 * @param username 用户名
	 * @return 用户信息
	 */
	UserDetailsEntity selectUserWithAuthorities(String username);
	
}