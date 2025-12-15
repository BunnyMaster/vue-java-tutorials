package com.spring.step3.security.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.spring.step3.domain.entity.PermissionEntity;
import com.spring.step3.domain.entity.RoleEntity;
import com.spring.step3.domain.entity.UserEntity;
import com.spring.step3.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@DS("testJwt")
@Service
@Transactional
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

        Long userId = userEntity.getId();

        List<String> list = new ArrayList<>();
        // 设置用户角色
        List<String> roles = findUserRolesByUserId(userId);
        // 设置用户权限
        List<String> permissions = findPermissionByUserId(userId);
        list.addAll(roles);
        list.addAll(permissions);

        Set<SimpleGrantedAuthority> authorities = list.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        // 设置用户权限
        userEntity.setAuthorities(authorities);
        // 返回时将用户密码置为空
        userEntity.setPassword(null);
        return userEntity;
    }

    /**
     * 根据用户id查找该用户的角色内容
     *
     * @param userId 用户id
     * @return 当前用户的角色信息
     */
    public List<String> findUserRolesByUserId(Long userId) {
        List<RoleEntity> roleList = userMapper.selectRolesByUserId(userId);
        return roleList.stream().map(RoleEntity::getRoleCode).toList();
    }

    /**
     * 根据用户id查找该用户的权限内容
     *
     * @param userId 用户id
     * @return 当前用户的权限信息
     */
    public List<String> findPermissionByUserId(Long userId) {
        List<PermissionEntity> permissionList = userMapper.selectPermissionByUserId(userId);
        return permissionList.stream().map(PermissionEntity::getPermissionCode).toList();
    }
}
