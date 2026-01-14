package com.spring.step3.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.step3.domain.dto.LoginDto;
import com.spring.step3.domain.entity.UserEntity;
import com.spring.step3.domain.vo.LoginVo;

public interface LoginService extends IService<UserEntity> {

    /**
     * 用户登录
     *
     * @param loginDto LoginRequest登录参数
     * @return 登录成功返回内容
     */
    LoginVo login(LoginDto loginDto);
}
