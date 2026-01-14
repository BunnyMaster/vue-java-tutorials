package com.spring.official.service.system.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.official.domain.dto.system.UserDto;
import com.spring.official.domain.entity.UserEntity;
import com.spring.official.domain.vo.UserVo;
import com.spring.official.domain.vo.result.PageResult;
import com.spring.official.mapper.UserMapper;
import com.spring.official.service.system.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    /**
     * 用户信息 服务实现类
     *
     * @param pageParams 用户信息分页查询page对象
     * @param dto        用户信息分页查询对象
     * @return 查询分页用户信息返回对象
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
     * 添加用户信息
     *
     * @param dto 用户信息添加
     */
    @Override
    public void addUser(UserDto dto) {
        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(dto, user);
        save(user);
    }

    /**
     * 更新用户信息
     *
     * @param dto 用户信息更新
     */
    @Override
    public void updateUser(UserDto dto) {
        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(dto, user);
        updateById(user);
    }

    /**
     * 删除|批量删除用户信息
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteUser(List<Long> ids) {
        removeByIds(ids);
    }
}