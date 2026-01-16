package example.security.authentication.jdbc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import example.security.authentication.jdbc.model.UserDetailsEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 用户详情服务，用于表单登录
 *
 * @author bunny
 */
public interface FormUserDetailsService extends IService<UserDetailsEntity>, UserDetailsService {

}
