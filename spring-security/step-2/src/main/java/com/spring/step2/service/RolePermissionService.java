package com.spring.step2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.step2.domain.dto.RolePermissionDto;
import com.spring.step2.domain.entity.RolePermissionEntity;
import com.spring.step2.domain.vo.RolePermissionVo;
import com.spring.step2.domain.vo.result.PageResult;

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
}
