package com.spring.step2.security.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.spring.step2.domain.entity.PermissionEntity;
import com.spring.step2.domain.entity.RoleEntity;
import com.spring.step2.domain.entity.UserEntity;
import com.spring.step2.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        // 设置用户角色
        String[] roles = findUserRolesByUserId(userId).toArray(String[]::new);

        // 设置用户权限
        List<String> permissionsByUserId = findPermissionByUserId(userId);
        String[] permissions = permissionsByUserId.toArray(String[]::new);

        // 也可以转成下面的形式
        // List<String> permissions = permissionsByUserId.stream()
        //         .map(SimpleGrantedAuthority::new)
        //         .toList();

        String[] authorities = ArrayUtils.addAll(roles, permissions);

        // 设置用户权限
        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                // 设置用户 authorities
                .authorities(authorities)
                .roles(roles)
                .build();
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
