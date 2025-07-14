package com.spring.step2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.step2.domain.dto.permission.PermissionDto;
import com.spring.step2.domain.entity.PermissionEntity;
import com.spring.step2.domain.vo.PermissionVo;
import com.spring.step2.domain.vo.result.PageResult;

import java.util.List;

/**
 * <p>
 * 系统权限表 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 22:36:53
 */
public interface PermissionService extends IService<PermissionEntity> {

    /**
     * 分页查询系统权限表
     *
     * @return {@link PermissionVo}
     */
    PageResult<PermissionVo> getPermissionPage(Page<PermissionEntity> pageParams, PermissionDto dto);

    /**
     * 添加系统权限表
     *
     * @param dto {@link PermissionDto} 添加表单
     */
    void addPermission(PermissionDto dto);

    /**
     * 更新系统权限表
     *
     * @param dto {@link PermissionDto} 更新表单
     */
    void updatePermission(PermissionDto dto);

    /**
     * 删除|批量删除系统权限表类型
     *
     * @param ids 删除id列表
     */
    void deletePermission(List<Long> ids);

    /**
     * 获取所有的权限列表
     *
     * @return 所有权限列表
     */
    List<PermissionVo> getAllPermission();

}
