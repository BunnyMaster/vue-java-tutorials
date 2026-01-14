package com.spring.official.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.official.domain.dto.system.RoleDto;
import com.spring.official.domain.entity.RoleEntity;
import com.spring.official.domain.vo.RoleVo;
import com.spring.official.domain.vo.result.PageResult;

import java.util.List;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
public interface RoleService extends IService<RoleEntity> {

    /**
     * 分页查询系统角色表
     *
     * @return {@link RoleVo}
     */
    PageResult<RoleVo> getRolePage(Page<RoleEntity> pageParams, RoleDto dto);

    /**
     * 添加系统角色表
     *
     * @param dto {@link RoleDto} 添加表单
     */
    void addRole(RoleDto dto);

    /**
     * 更新系统角色表
     *
     * @param dto {@link RoleDto} 更新表单
     */
    void updateRole(RoleDto dto);

    /**
     * 删除|批量删除系统角色表类型
     *
     * @param ids 删除id列表
     */
    void deleteRole(List<Long> ids);
}
