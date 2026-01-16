package example.security.authentication.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import example.security.authentication.customer.model.UserDetailsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
	UserDetailsEntity selectUserByUsername(@Param("username") String username);

	/**
	 * 根据邮箱查询用户信息
	 *
	 * @param email 邮箱
	 * @return 用户信息
	 */
	UserDetailsEntity selectUserByEmail(@Param("email") String email);

	/**
	 * 根据手机号查询用户信息
	 *
	 * @param phone 手机号
	 * @return 用户信息
	 */
	UserDetailsEntity selectUserByPhone(@Param("phone") String phone);
}