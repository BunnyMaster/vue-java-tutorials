package com.spring.step2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.step2.domain.dto.role.RoleDto;
import com.spring.step2.domain.entity.RoleEntity;
import com.spring.step2.domain.vo.RoleVo;
import com.spring.step2.domain.vo.result.PageResult;

import java.util.List;


/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 22:36:53
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

    /**
     * 获取全部角色列表
     *
     * @return 角色列表
     */
    List<RoleVo> getRoleList();

}
