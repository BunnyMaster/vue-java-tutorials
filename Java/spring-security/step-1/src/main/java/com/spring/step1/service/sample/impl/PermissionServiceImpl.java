package com.spring.step1.service.sample.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.step1.bean.dto.system.PermissionDto;
import com.spring.step1.bean.entity.PermissionEntity;
import com.spring.step1.bean.vo.PermissionVo;
import com.spring.step1.bean.vo.result.PageResult;
import com.spring.step1.mapper.PermissionMapper;
import com.spring.step1.service.sample.PermissionService;
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
 * @since 2025-07-10 12:02:29
 */
@Service
@Transactional
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
}