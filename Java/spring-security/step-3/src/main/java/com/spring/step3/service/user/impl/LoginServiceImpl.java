package com.spring.step3.service.user.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.step3.domain.dto.LoginDto;
import com.spring.step3.domain.entity.UserEntity;
import com.spring.step3.domain.enums.LoginEnums;
import com.spring.step3.domain.vo.LoginVo;
import com.spring.step3.domain.vo.result.ResultCodeEnum;
import com.spring.step3.mapper.UserMapper;
import com.spring.step3.security.service.DbUserDetailService;
import com.spring.step3.security.service.JwtTokenService;
import com.spring.step3.service.user.LoginService;
import com.spring.step3.service.user.strategy.DefaultLoginStrategy;
import com.spring.step3.service.user.strategy.LoginContext;
import com.spring.step3.service.user.strategy.LoginStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@DS("testJwt")
@Service
@RequiredArgsConstructor
public class LoginServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements LoginService {

    private final JwtTokenService jwtTokenService;
    private final DbUserDetailService dbUserDetailService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 用户登录
     *
     * @param loginDto LoginRequest登录参数
     * @return 登录成功返回内容
     */
    @Override
    public LoginVo login(LoginDto loginDto) {
        // 初始化所有策略（可扩展）
        HashMap<String, LoginStrategy> loginStrategyHashMap = new HashMap<>();
        // 默认的登录方式
        loginStrategyHashMap.put(LoginEnums.default_STRATEGY.getValue(), new DefaultLoginStrategy(userMapper));

        // 使用登录上下文调用登录策略
        LoginContext loginContext = new LoginContext(loginStrategyHashMap);
        UserEntity user = loginContext.executeStrategy(loginDto);

        // 验证登录逻辑
        if (user == null) throw new UsernameNotFoundException(ResultCodeEnum.USER_IS_EMPTY.getMessage());

        // 数据库密码
        String dbPassword = user.getPassword();
        String password = loginDto.getPassword();
        if (!passwordEncoder.matches(password, dbPassword)) {
            throw new UsernameNotFoundException(ResultCodeEnum.LOGIN_ERROR.getMessage());
        }

        // 登录结束后的操作
        loginContext.loginAfter(loginDto, user);

        // 设置用户创建用户id 和 更新用户id
        Long userId = user.getId();
        user.setCreateUser(userId);
        user.setUpdateUser(userId);
        updateById(user);

        List<String> roles = dbUserDetailService.findUserRolesByUserId(userId);
        List<String> permission = dbUserDetailService.findPermissionByUserId(userId);
        String token = jwtTokenService.createToken(userId, user.getUsername(), roles, permission);

        // 过期时间
        Long expiresInSeconds = jwtTokenService.expired;
        long expirationMillis = System.currentTimeMillis() + (expiresInSeconds * 1000);
        Date date = new Date(expirationMillis);

        // 构建用户返回对象
        LoginVo loginVo = new LoginVo();
        BeanUtils.copyProperties(user, loginVo);
        loginVo.setToken(token);
        loginVo.setReadMeDay(expirationMillis);
        loginVo.setExpires(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));

        return loginVo;
    }
}
