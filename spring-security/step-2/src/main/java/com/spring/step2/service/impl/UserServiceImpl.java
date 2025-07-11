package com.spring.step2.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.step2.domain.dto.UserDto;
import com.spring.step2.domain.entity.UserEntity;
import com.spring.step2.domain.vo.UserVo;
import com.spring.step2.domain.vo.result.PageResult;
import com.spring.step2.mapper.UserMapper;
import com.spring.step2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 14:49:46
 */
@DS("testJwt")
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    private final PasswordEncoder passwordEncoder;

    /**
     * * 用户 服务实现类
     *
     * @param pageParams 用户分页查询page对象
     * @param dto        用户分页查询对象
     * @return 查询分页用户返回对象
     */
    @Override
    public PageResult<UserVo> getUserPage(Page<UserEntity> pageParams, UserDto dto) {
        IPage<UserVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<UserVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加用户
     *
     * @param dto 用户添加
     */
    @Override
    public void addUser(UserDto dto) {
        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(dto, user);

        // 设置用户密码
        String password = user.getPassword();
        String encodePassword = passwordEncoder.encode(password);
        user.setPassword(encodePassword);

        save(user);
    }

    /**
     * 更新用户
     *
     * @param dto 用户更新
     */
    @Override
    public void updateUser(UserDto dto) {
        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(dto, user);
        updateById(user);
    }

    /**
     * 删除|批量删除用户
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteUser(List<Long> ids) {
        removeByIds(ids);
    }
}