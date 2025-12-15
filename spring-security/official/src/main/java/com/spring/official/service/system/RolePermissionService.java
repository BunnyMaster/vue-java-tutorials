package com.spring.official.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.official.domain.dto.system.RolePermissionDto;
import com.spring.official.domain.entity.RolePermissionEntity;
import com.spring.official.domain.vo.RolePermissionVo;
import com.spring.official.domain.vo.result.PageResult;

import java.util.List;

/**
 * <p>
 * 系统角色权限表 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
public interface RolePermissionService extends IService<RolePermissionEntity> {

    /**
     * 分页查询系统角色权限表
     *
     * @return {@link RolePermissionVo}
     */
    PageResult<RolePermissionVo> getRolePermissionPage(Page<RolePermissionEntity> pageParams, RolePermissionDto dto);

    /**
     * 添加系统角色权限表
     *
     * @param dto {@link RolePermissionDto} 添加表单
     */
    void addRolePermission(RolePermissionDto dto);

    /**
     * 更新系统角色权限表
     *
     * @param dto {@link RolePermissionDto} 更新表单
     */
    void updateRolePermission(RolePermissionDto dto);

    /**
     * 删除|批量删除系统角色权限表类型
     *
     * @param ids 删除id列表
     */
    void deleteRolePermission(List<Long> ids);
}
