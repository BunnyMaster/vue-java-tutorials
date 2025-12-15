package com.spring.step3.service.user.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.step3.domain.dto.user.UserDto;
import com.spring.step3.domain.entity.UserEntity;
import com.spring.step3.domain.vo.UserVo;
import com.spring.step3.domain.vo.result.PageResult;
import com.spring.step3.mapper.UserMapper;
import com.spring.step3.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 用户基本信息表 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 22:36:53
 */
@DS("testJwt")
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    private final PasswordEncoder passwordEncoder;

    /**
     * 用户基本信息表 服务实现类
     *
     * @param pageParams 用户基本信息表分页查询page对象
     * @param dto        用户基本信息表分页查询对象
     * @return 查询分页用户基本信息表返回对象
     */
    @Override
    public PageResult<UserVo> getUserPage(Page<UserEntity> pageParams, UserDto dto) {
        IPage<UserVo> page = baseMapper.selectListByPage(pageParams, dto);


        return PageResult.<UserVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .pages(page.getPages())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加用户基本信息表
     *
     * @param dto 用户基本信息表添加
     */
    @Override
    public void addUser(UserDto dto) {
        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(dto, user);

        // 用户密码是否为空，为空设置默认密码
        String password = user.getPassword();
        password = StringUtils.hasText(password) ? password : "123456";

        // 设置用户密码
        String encodePassword = passwordEncoder.encode(password);
        user.setPassword(encodePassword);

        save(user);
    }

    /**
     * 更新用户基本信息表
     *
     * @param dto 用户基本信息表更新
     */
    @Override
    public void updateUser(UserDto dto) {
        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(dto, user);

        // 设置用户密码
        String password = user.getPassword();
        if (StringUtils.hasText(password)) {
            String encodePassword = passwordEncoder.encode(password);
            user.setPassword(encodePassword);
        }

        updateById(user);
    }

    /**
     * 删除|批量删除用户基本信息表
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteUser(List<Long> ids) {
        removeByIds(ids);
    }
}