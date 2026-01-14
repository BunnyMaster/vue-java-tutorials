package com.spring.step3.service.roles;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.step3.domain.dto.role.AssignRolePermissionDto;
import com.spring.step3.domain.dto.role.RolePermissionDto;
import com.spring.step3.domain.entity.RolePermissionEntity;
import com.spring.step3.domain.vo.RolePermissionVo;
import com.spring.step3.domain.vo.result.PageResult;
import jakarta.validation.Valid;

import java.util.List;

/**
 * <p>
 * 角色权限关联表 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 22:36:53
 */
public interface RolePermissionService extends IService<RolePermissionEntity> {

    /**
     * 分页查询角色权限关联表
     *
     * @return {@link RolePermissionVo}
     */
    PageResult<RolePermissionVo> getRolePermissionPage(Page<RolePermissionEntity> pageParams, RolePermissionDto dto);

    /**
     * 添加角色权限关联表
     *
     * @param dto {@link RolePermissionDto} 添加表单
     */
    void addRolePermission(RolePermissionDto dto);

    /**
     * 更新角色权限关联表
     *
     * @param dto {@link RolePermissionDto} 更新表单
     */
    void updateRolePermission(RolePermissionDto dto);

    /**
     * 删除|批量删除角色权限关联表类型
     *
     * @param ids 删除id列表
     */
    void deleteRolePermission(List<Long> ids);

    /**
     * 根据角色id获取权限内容
     *
     * @param permissionId 权限id
     * @return 角色权限列表
     */
    List<RolePermissionVo> getRolePermissionById(Long permissionId);

    /**
     * 根据角色id分配权限
     *
     * @param dto 为角色分配权限 {@link AssignRolePermissionDto}
     */
    void assignRolePermission(@Valid AssignRolePermissionDto dto);
}
