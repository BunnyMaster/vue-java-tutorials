package com.spring.step2.service.user.strategy;

import com.spring.step2.domain.dto.LoginDto;
import com.spring.step2.domain.entity.UserEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

/**
 * 登录策略上下文
 */
public class LoginContext {

    private final Map<String, LoginStrategy> strategies;

    public LoginContext(Map<String, LoginStrategy> strategies) {
        this.strategies = strategies;
    }


    /**
     * 执行登录策略
     * 根据情况判断 type 是否为空
     *
     * @param loginDto 登录参数
     * @return 用户
     */
    public UserEntity executeStrategy(LoginDto loginDto) {
        String type = loginDto.getType();
        LoginStrategy strategy = strategies.get(type);

        if (strategy == null) {
            throw new UsernameNotFoundException("不支持登录类型: " + type);
        }

        return strategy.authenticate(loginDto);
    }

    /**
     * 登录完成后的内容
     *
     * @param loginDto 登录参数
     */
    public void loginAfter(LoginDto loginDto, UserEntity adminUser) {
        String type = loginDto.getType();
        LoginStrategy strategy = strategies.get(type);

        strategy.authenticateAfter(loginDto, adminUser);
    }
}