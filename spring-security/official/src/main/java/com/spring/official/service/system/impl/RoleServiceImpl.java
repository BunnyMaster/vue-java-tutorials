package com.spring.official.service.system.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.official.domain.dto.system.RoleDto;
import com.spring.official.domain.entity.RoleEntity;
import com.spring.official.domain.vo.RoleVo;
import com.spring.official.domain.vo.result.PageResult;
import com.spring.official.mapper.RoleMapper;
import com.spring.official.service.system.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Service
@Transactional
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements RoleService {

    /**
     * 系统角色表 服务实现类
     *
     * @param pageParams 系统角色表分页查询page对象
     * @param dto        系统角色表分页查询对象
     * @return 查询分页系统角色表返回对象
     */
    @Override
    public PageResult<RoleVo> getRolePage(Page<RoleEntity> pageParams, RoleDto dto) {
        IPage<RoleVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<RoleVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加系统角色表
     *
     * @param dto 系统角色表添加
     */
    @Override
    public void addRole(RoleDto dto) {
        RoleEntity role = new RoleEntity();
        BeanUtils.copyProperties(dto, role);
        save(role);
    }

    /**
     * 更新系统角色表
     *
     * @param dto 系统角色表更新
     */
    @Override
    public void updateRole(RoleDto dto) {
        RoleEntity role = new RoleEntity();
        BeanUtils.copyProperties(dto, role);
        updateById(role);
    }

    /**
     * 删除|批量删除系统角色表
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteRole(List<Long> ids) {
        removeByIds(ids);
    }
}