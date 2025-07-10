package com.spring.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.domain.dto.PermissionDto;
import com.spring.domain.entity.PermissionEntity;
import com.spring.domain.vo.PermissionVo;
import com.spring.domain.vo.result.PageResult;
import com.spring.domain.vo.result.Result;
import com.spring.domain.vo.result.ResultCodeEnum;
import java.util.List;

/**
 * <p>
 * 系统权限表 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
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
}
