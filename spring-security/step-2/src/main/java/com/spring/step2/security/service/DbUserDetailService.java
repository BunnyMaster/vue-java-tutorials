package com.spring.step2.security.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.spring.step2.domain.entity.PermissionEntity;
import com.spring.step2.domain.entity.UserEntity;
import com.spring.step2.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@DS("testJwt")
@Service
@RequiredArgsConstructor
public class DbUserDetailService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询当前用户
        UserEntity userEntity = userMapper.selectByUsername(username);

        // 判断当前用户是否存在
        if (userEntity == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 设置用户权限
        List<SimpleGrantedAuthority> authorities = findPermissionByUserId(userEntity.getId()).stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(authorities)
                .build();
    }

    public List<String> findPermissionByUserId(Long userId) {
        List<PermissionEntity> permissionList = userMapper.selectPermissionByUserId(userId);
        return permissionList.stream().map(PermissionEntity::getPermissionCode).toList();
    }
}
