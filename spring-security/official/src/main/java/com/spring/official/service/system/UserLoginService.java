package com.spring.official.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.official.domain.dto.system.UserLoginDto;
import com.spring.official.domain.entity.UserLoginEntity;
import com.spring.official.domain.vo.UserLoginVo;
import com.spring.official.domain.vo.result.PageResult;

import java.util.List;

/**
 * <p>
 * 用户登录日志 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
public interface UserLoginService extends IService<UserLoginEntity> {

    /**
     * 分页查询用户登录日志
     *
     * @return {@link UserLoginVo}
     */
    PageResult<UserLoginVo> getUserLoginPage(Page<UserLoginEntity> pageParams, UserLoginDto dto);

    /**
     * 添加用户登录日志
     *
     * @param dto {@link UserLoginDto} 添加表单
     */
    void addUserLogin(UserLoginDto dto);

    /**
     * 更新用户登录日志
     *
     * @param dto {@link UserLoginDto} 更新表单
     */
    void updateUserLogin(UserLoginDto dto);

    /**
     * 删除|批量删除用户登录日志类型
     *
     * @param ids 删除id列表
     */
    void deleteUserLogin(List<Long> ids);
}
