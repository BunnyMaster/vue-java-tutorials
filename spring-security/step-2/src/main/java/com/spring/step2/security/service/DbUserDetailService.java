package com.spring.step2.security.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spring.step2.domain.entity.UserEntity;
import com.spring.step2.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DbUserDetailService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询当前用户
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserEntity::getUsername, username);
        UserEntity userEntity = userMapper.selectOne(wrapper);

        // 判断当前用户是否存在
        if (userEntity == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        return User.builder().username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .build();
    }
}
