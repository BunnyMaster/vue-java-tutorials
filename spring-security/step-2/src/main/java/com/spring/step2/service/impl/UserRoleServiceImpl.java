package com.spring.step2.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.step2.domain.dto.UserRoleDto;
import com.spring.step2.domain.entity.UserRoleEntity;
import com.spring.step2.domain.vo.UserRoleVo;
import com.spring.step2.domain.vo.result.PageResult;
import com.spring.step2.mapper.UserRoleMapper;
import com.spring.step2.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户角色关联表 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 22:36:53
 */
@DS("testJwt")
@Service
@Transactional
@RequiredArgsConstructor
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleEntity> implements UserRoleService {

    /**
     * * 用户角色关联表 服务实现类
     *
     * @param pageParams 用户角色关联表分页查询page对象
     * @param dto        用户角色关联表分页查询对象
     * @return 查询分页用户角色关联表返回对象
     */
    @Override
    public PageResult<UserRoleVo> getUserRolePage(Page<UserRoleEntity> pageParams, UserRoleDto dto) {
        IPage<UserRoleVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<UserRoleVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .pages(page.getPages())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加用户角色关联表
     *
     * @param dto 用户角色关联表添加
     */
    @Override
    public void addUserRole(UserRoleDto dto) {
        UserRoleEntity userRole = new UserRoleEntity();
        BeanUtils.copyProperties(dto, userRole);
        save(userRole);
    }

    /**
     * 更新用户角色关联表
     *
     * @param dto 用户角色关联表更新
     */
    @Override
    public void updateUserRole(UserRoleDto dto) {
        UserRoleEntity userRole = new UserRoleEntity();
        BeanUtils.copyProperties(dto, userRole);
        updateById(userRole);
    }

    /**
     * 删除|批量删除用户角色关联表
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteUserRole(List<Long> ids) {
        removeByIds(ids);
    }
}