package com.spring.step3.service.user;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.step3.domain.dto.user.AssignUserRoleDto;
import com.spring.step3.domain.dto.user.UserRoleDto;
import com.spring.step3.domain.entity.UserRoleEntity;
import com.spring.step3.domain.vo.UserRoleVo;
import com.spring.step3.domain.vo.result.PageResult;
import jakarta.validation.Valid;

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

    /**
     * 根据用户id获取当前用户角色列表
     *
     * @param userId 用户id
     * @return 用户和角色列表
     */
    List<UserRoleVo> getRoleListByUserId(Long userId);

    /**
     * 根据用户id分配用户角色
     *
     * @param dto 用户分配角色DTO {@link AssignUserRoleDto}
     */
    void assignUserRole(@Valid AssignUserRoleDto dto);
}
