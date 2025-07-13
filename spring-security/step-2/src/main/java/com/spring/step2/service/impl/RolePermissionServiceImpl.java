package com.spring.step2.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.step2.domain.dto.RolePermissionDto;
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
     * * 角色权限关联表 服务实现类
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
}