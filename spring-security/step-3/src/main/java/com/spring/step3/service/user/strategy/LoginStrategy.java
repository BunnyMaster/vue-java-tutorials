package com.spring.step3.service.user.strategy;

import com.spring.step3.domain.dto.LoginDto;
import com.spring.step3.domain.entity.UserEntity;

/**
 * 登录策略
 */
public interface LoginStrategy {

    /**
     * 登录鉴定方法
     *
     * @param loginDto 登录参数
     * @return 鉴定身份验证
     */
    UserEntity authenticate(LoginDto loginDto);

    /**
     * 登录完成后的内容
     *
     * @param loginDto  登录参数
     * @param adminUser {@link UserEntity}
     */
    void authenticateAfter(LoginDto loginDto, UserEntity adminUser);
}
