package com.spring.step2.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.step2.domain.dto.role.AssignRolePermissionDto;
import com.spring.step2.domain.dto.role.RolePermissionDto;
import com.spring.step2.domain.entity.RolePermissionEntity;
import com.spring.step2.domain.vo.RolePermissionVo;
import com.spring.step2.domain.vo.result.PageResult;
import com.spring.step2.mapper.RolePermissionMapper;
import com.spring.step2.service.RolePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 角色权限关联表 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 22:36:53
 */
@DS("testJwt")
@Service
@Transactional
@RequiredArgsConstructor
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermissionEntity> implements RolePermissionService {

    /**
     * 角色权限关联表 服务实现类
     *
     * @param pageParams 角色权限关联表分页查询page对象
     * @param dto        角色权限关联表分页查询对象
     * @return 查询分页角色权限关联表返回对象
     */
    @Override
    public PageResult<RolePermissionVo> getRolePermissionPage(Page<RolePermissionEntity> pageParams, RolePermissionDto dto) {
        IPage<RolePermissionVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<RolePermissionVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .pages(page.getPages())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加角色权限关联表
     *
     * @param dto 角色权限关联表添加
     */
    @Override
    public void addRolePermission(RolePermissionDto dto) {
        RolePermissionEntity rolePermission = new RolePermissionEntity();
        BeanUtils.copyProperties(dto, rolePermission);
        save(rolePermission);
    }

    /**
     * 更新角色权限关联表
     *
     * @param dto 角色权限关联表更新
     */
    @Override
    public void updateRolePermission(RolePermissionDto dto) {
        RolePermissionEntity rolePermission = new RolePermissionEntity();
        BeanUtils.copyProperties(dto, rolePermission);
        updateById(rolePermission);
    }

    /**
     * 删除|批量删除角色权限关联表
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteRolePermission(List<Long> ids) {
        removeByIds(ids);
    }

    /**
     * 根据角色id获取权限内容
     *
     * @param permissionId 权限id
     * @return 角色权限列表
     */
    @Override
    public List<RolePermissionVo> getRolePermissionById(Long permissionId) {
        List<RolePermissionEntity> list = baseMapper.selectListByPermissionId(permissionId);

        return list.stream().map(rolePermissionEntity -> {
            RolePermissionVo rolePermissionVo = new RolePermissionVo();
            BeanUtils.copyProperties(rolePermissionEntity, rolePermissionVo);
            return rolePermissionVo;
        }).toList();
    }

    /**
     * 根据角色id分配权限
     *
     * @param dto 为角色分配权限 {@link AssignRolePermissionDto}
     */
    @Override
    public void assignRolePermission(AssignRolePermissionDto dto) {
        Long roleId = dto.getRoleId();
        List<Long> permissionIds = dto.getPermissionIds();

        // 先删除当前已经分配的角色权限内容
        baseMapper.deleteByRoleId(roleId);

        List<RolePermissionEntity> rolePermissionEntityList = permissionIds.stream().map(permissionId -> {
            RolePermissionEntity rolePermissionEntity = new RolePermissionEntity();
            rolePermissionEntity.setRoleId(roleId);
            rolePermissionEntity.setPermissionId(permissionId);
            return rolePermissionEntity;
        }).toList();

        saveBatch(rolePermissionEntityList);
    }
}