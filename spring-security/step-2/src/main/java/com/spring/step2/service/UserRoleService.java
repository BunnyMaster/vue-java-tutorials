package com.spring.step2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.step2.domain.dto.UserRoleDto;
import com.spring.step2.domain.entity.UserRoleEntity;
import com.spring.step2.domain.vo.UserRoleVo;
import com.spring.step2.domain.vo.result.PageResult;

import java.util.List;

/**
 * <p>
 * 用户角色关联表 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 22:36:53
 */
public interface UserRoleService extends IService<UserRoleEntity> {

    /**
     * 分页查询用户角色关联表
     *
     * @return {@link UserRoleVo}
     */
    PageResult<UserRoleVo> getUserRolePage(Page<UserRoleEntity> pageParams, UserRoleDto dto);

    /**
     * 添加用户角色关联表
     *
     * @param dto {@link UserRoleDto} 添加表单
     */
    void addUserRole(UserRoleDto dto);

    /**
     * 更新用户角色关联表
     *
     * @param dto {@link UserRoleDto} 更新表单
     */
    void updateUserRole(UserRoleDto dto);

    /**
     * 删除|批量删除用户角色关联表类型
     *
     * @param ids 删除id列表
     */
    void deleteUserRole(List<Long> ids);
}
