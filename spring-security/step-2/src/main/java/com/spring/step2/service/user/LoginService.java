package com.spring.step2.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.step2.domain.dto.LoginDto;
import com.spring.step2.domain.entity.UserEntity;
import com.spring.step2.domain.vo.LoginVo;

public interface LoginService extends IService<UserEntity> {

    /**
     * 用户登录
     *
     * @param loginDto LoginRequest登录参数
     * @return 登录成功返回内容
     */
    LoginVo login(LoginDto loginDto);
}
