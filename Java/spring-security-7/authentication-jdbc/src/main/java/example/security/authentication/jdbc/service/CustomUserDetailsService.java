package example.security.authentication.jdbc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import example.security.authentication.jdbc.model.UserDetailsEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 用户详情服务
 *
 * @author bunny
 */
public interface CustomUserDetailsService extends IService<UserDetailsEntity>, UserDetailsService {
}
