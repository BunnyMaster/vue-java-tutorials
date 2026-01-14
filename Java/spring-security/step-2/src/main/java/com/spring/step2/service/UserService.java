package com.spring.step2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.step2.domain.dto.user.UserDto;
import com.spring.step2.domain.entity.UserEntity;
import com.spring.step2.domain.vo.UserVo;
import com.spring.step2.domain.vo.result.PageResult;

import java.util.List;

/**
 * <p>
 * 用户基本信息表 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 22:36:53
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 分页查询用户基本信息表
     *
     * @return {@link UserVo}
     */
    PageResult<UserVo> getUserPage(Page<UserEntity> pageParams, UserDto dto);

    /**
     * 添加用户基本信息表
     *
     * @param dto {@link UserDto} 添加表单
     */
    void addUser(UserDto dto);

    /**
     * 更新用户基本信息表
     *
     * @param dto {@link UserDto} 更新表单
     */
    void updateUser(UserDto dto);

    /**
     * 删除|批量删除用户基本信息表类型
     *
     * @param ids 删除id列表
     */
    void deleteUser(List<Long> ids);
}
