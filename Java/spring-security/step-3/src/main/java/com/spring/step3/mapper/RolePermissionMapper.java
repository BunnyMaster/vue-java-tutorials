package com.spring.step3.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step3.domain.dto.role.RolePermissionDto;
import com.spring.step3.domain.entity.RolePermissionEntity;
import com.spring.step3.domain.vo.RolePermissionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色权限关联表 Mapper 接口
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 22:36:53
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermissionEntity> {

    /**
     * 分页查询角色权限关联表内容
     *
     * @param pageParams 角色权限关联表分页参数
     * @param dto        角色权限关联表查询表单
     * @return 角色权限关联表分页结果
     */
    IPage<RolePermissionVo> selectListByPage(@Param("page") Page<RolePermissionEntity> pageParams, @Param("dto") RolePermissionDto dto);

    /**
     * 根据角色id获取权限内容
     *
     * @param permissionId 权限id
     * @return 角色权限列表
     */
    List<RolePermissionEntity> selectListByPermissionId(Long permissionId);

    /**
     * 先删除当前已经分配的角色权限内容
     *
     * @param roleId 角色id
     */
    void deleteByRoleId(Long roleId);
}
