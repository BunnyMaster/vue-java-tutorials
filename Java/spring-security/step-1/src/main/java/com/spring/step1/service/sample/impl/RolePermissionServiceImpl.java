package com.spring.step1.service.sample.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.step1.bean.dto.system.RolePermissionDto;
import com.spring.step1.bean.entity.RolePermissionEntity;
import com.spring.step1.bean.vo.RolePermissionVo;
import com.spring.step1.bean.vo.result.PageResult;
import com.spring.step1.mapper.RolePermissionMapper;
import com.spring.step1.service.sample.RolePermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 系统角色权限表 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Service
@Transactional
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermissionEntity> implements RolePermissionService {

    /**
     * 系统角色权限表 服务实现类
     *
     * @param pageParams 系统角色权限表分页查询page对象
     * @param dto        系统角色权限表分页查询对象
     * @return 查询分页系统角色权限表返回对象
     */
    @Override
    public PageResult<RolePermissionVo> getRolePermissionPage(Page<RolePermissionEntity> pageParams, RolePermissionDto dto) {
        IPage<RolePermissionVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<RolePermissionVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加系统角色权限表
     *
     * @param dto 系统角色权限表添加
     */
    @Override
    public void addRolePermission(RolePermissionDto dto) {
        RolePermissionEntity rolePermission = new RolePermissionEntity();
        BeanUtils.copyProperties(dto, rolePermission);
        save(rolePermission);
    }

    /**
     * 更新系统角色权限表
     *
     * @param dto 系统角色权限表更新
     */
    @Override
    public void updateRolePermission(RolePermissionDto dto) {
        RolePermissionEntity rolePermission = new RolePermissionEntity();
        BeanUtils.copyProperties(dto, rolePermission);
        updateById(rolePermission);
    }

    /**
     * 删除|批量删除系统角色权限表
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteRolePermission(List<Long> ids) {
        removeByIds(ids);
    }
}