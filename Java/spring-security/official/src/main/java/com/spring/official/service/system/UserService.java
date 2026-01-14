package com.spring.official.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.official.domain.dto.system.UserDto;
import com.spring.official.domain.entity.UserEntity;
import com.spring.official.domain.vo.UserVo;
import com.spring.official.domain.vo.result.PageResult;

import java.util.List;

/**
 * <p>
 * 用户信息 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 分页查询用户信息
     *
     * @return {@link UserVo}
     */
    PageResult<UserVo> getUserPage(Page<UserEntity> pageParams, UserDto dto);

    /**
     * 添加用户信息
     *
     * @param dto {@link UserDto} 添加表单
     */
    void addUser(UserDto dto);

    /**
     * 更新用户信息
     *
     * @param dto {@link UserDto} 更新表单
     */
    void updateUser(UserDto dto);

    /**
     * 删除|批量删除用户信息类型
     *
     * @param ids 删除id列表
     */
    void deleteUser(List<Long> ids);
}
