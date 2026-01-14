package com.spring.step1.service.sample.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.step1.bean.dto.system.RouterRoleDto;
import com.spring.step1.bean.entity.RouterRoleEntity;
import com.spring.step1.bean.vo.RouterRoleVo;
import com.spring.step1.bean.vo.result.PageResult;
import com.spring.step1.mapper.RouterRoleMapper;
import com.spring.step1.service.sample.RouterRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 系统路由角色关系表 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Service
@Transactional
public class RouterRoleServiceImpl extends ServiceImpl<RouterRoleMapper, RouterRoleEntity> implements RouterRoleService {

    /**
     * 系统路由角色关系表 服务实现类
     *
     * @param pageParams 系统路由角色关系表分页查询page对象
     * @param dto        系统路由角色关系表分页查询对象
     * @return 查询分页系统路由角色关系表返回对象
     */
    @Override
    public PageResult<RouterRoleVo> getRouterRolePage(Page<RouterRoleEntity> pageParams, RouterRoleDto dto) {
        IPage<RouterRoleVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<RouterRoleVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加系统路由角色关系表
     *
     * @param dto 系统路由角色关系表添加
     */
    @Override
    public void addRouterRole(RouterRoleDto dto) {
        RouterRoleEntity routerRole = new RouterRoleEntity();
        BeanUtils.copyProperties(dto, routerRole);
        save(routerRole);
    }

    /**
     * 更新系统路由角色关系表
     *
     * @param dto 系统路由角色关系表更新
     */
    @Override
    public void updateRouterRole(RouterRoleDto dto) {
        RouterRoleEntity routerRole = new RouterRoleEntity();
        BeanUtils.copyProperties(dto, routerRole);
        updateById(routerRole);
    }

    /**
     * 删除|批量删除系统路由角色关系表
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteRouterRole(List<Long> ids) {
        removeByIds(ids);
    }
}