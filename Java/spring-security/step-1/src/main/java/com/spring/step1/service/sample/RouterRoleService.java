package com.spring.step1.service.sample;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.step1.bean.dto.system.RouterRoleDto;
import com.spring.step1.bean.entity.RouterRoleEntity;
import com.spring.step1.bean.vo.RouterRoleVo;
import com.spring.step1.bean.vo.result.PageResult;

import java.util.List;

/**
 * <p>
 * 系统路由角色关系表 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
public interface RouterRoleService extends IService<RouterRoleEntity> {

    /**
     * 分页查询系统路由角色关系表
     *
     * @return {@link RouterRoleVo}
     */
    PageResult<RouterRoleVo> getRouterRolePage(Page<RouterRoleEntity> pageParams, RouterRoleDto dto);

    /**
     * 添加系统路由角色关系表
     *
     * @param dto {@link RouterRoleDto} 添加表单
     */
    void addRouterRole(RouterRoleDto dto);

    /**
     * 更新系统路由角色关系表
     *
     * @param dto {@link RouterRoleDto} 更新表单
     */
    void updateRouterRole(RouterRoleDto dto);

    /**
     * 删除|批量删除系统路由角色关系表类型
     *
     * @param ids 删除id列表
     */
    void deleteRouterRole(List<Long> ids);
}
