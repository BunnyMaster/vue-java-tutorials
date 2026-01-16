package example.security.authentication.jdbc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 用户实体
 *
 * @author bunny
 */
@TableName("users")
@Data
public class UserDetailsEntity implements UserDetails, CredentialsContainer {

	/**
	 * 用户ID
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 状态
	 */
	private Boolean enabled;

	@TableField("account_non_expired")
	private Boolean accountNonExpired;

	@TableField("account_non_locked")
	private Boolean accountNonLocked;

	@TableField("credentials_non_expired")
	private Boolean credentialsNonExpired;

	/**
	 * 非数据库字段
	 */
	@TableField(exist = false)
	private List<GrantedAuthority> authorities;

	/**
	 * 密码重置（一定要清空！）
	 */
	@Override
	public void eraseCredentials() {
		this.password = null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities != null ? authorities : List.of();
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired != null ? accountNonExpired : true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked != null ? accountNonLocked : true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired != null ? credentialsNonExpired : true;
	}

	@Override
	public boolean isEnabled() {
		return enabled != null ? enabled : true;
	}
}