package com.spring.step2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.step2.domain.dto.UserDto;
import com.spring.step2.domain.entity.UserEntity;
import com.spring.step2.domain.vo.UserVo;
import com.spring.step2.domain.vo.result.PageResult;

import java.util.List;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 14:49:46
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 分页查询用户
     *
     * @return {@link UserVo}
     */
    PageResult<UserVo> getUserPage(Page<UserEntity> pageParams, UserDto dto);

    /**
     * 添加用户
     *
     * @param dto {@link UserDto} 添加表单
     */
    void addUser(UserDto dto);

    /**
     * 更新用户
     *
     * @param dto {@link UserDto} 更新表单
     */
    void updateUser(UserDto dto);

    /**
     * 删除|批量删除用户类型
     *
     * @param ids 删除id列表
     */
    void deleteUser(List<Long> ids);
}
