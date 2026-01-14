package com.spring.step2.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.step2.domain.dto.permission.PermissionDto;
import com.spring.step2.domain.entity.PermissionEntity;
import com.spring.step2.domain.vo.PermissionVo;
import com.spring.step2.domain.vo.result.PageResult;
import com.spring.step2.mapper.PermissionMapper;
import com.spring.step2.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 系统权限表 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 22:36:53
 */
@DS("testJwt")
@Service
@Transactional
@RequiredArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionEntity> implements PermissionService {

    /**
     * 系统权限表 服务实现类
     *
     * @param pageParams 系统权限表分页查询page对象
     * @param dto        系统权限表分页查询对象
     * @return 查询分页系统权限表返回对象
     */
    @Override
    public PageResult<PermissionVo> getPermissionPage(Page<PermissionEntity> pageParams, PermissionDto dto) {
        IPage<PermissionVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<PermissionVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .pages(page.getPages())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加系统权限表
     *
     * @param dto 系统权限表添加
     */
    @Override
    public void addPermission(PermissionDto dto) {
        PermissionEntity permission = new PermissionEntity();
        BeanUtils.copyProperties(dto, permission);
        save(permission);
    }

    /**
     * 更新系统权限表
     *
     * @param dto 系统权限表更新
     */
    @Override
    public void updatePermission(PermissionDto dto) {
        PermissionEntity permission = new PermissionEntity();
        BeanUtils.copyProperties(dto, permission);
        updateById(permission);
    }

    /**
     * 删除|批量删除系统权限表
     *
     * @param ids 删除id列表
     */
    @Override
    public void deletePermission(List<Long> ids) {
        removeByIds(ids);
    }

    /**
     * 获取所有的权限列表
     *
     * @return 所有权限列表
     */
    @Override
    public List<PermissionVo> getAllPermission() {
        return list().stream().map(permissionEntity -> {
            PermissionVo permissionVo = new PermissionVo();
            BeanUtils.copyProperties(permissionEntity, permissionVo);
            return permissionVo;
        }).toList();
    }
}