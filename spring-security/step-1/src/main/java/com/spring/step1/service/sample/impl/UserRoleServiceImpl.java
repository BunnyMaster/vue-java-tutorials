package com.spring.step1.service.sample.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.step1.bean.dto.system.UserRoleDto;
import com.spring.step1.bean.entity.UserRoleEntity;
import com.spring.step1.bean.vo.UserRoleVo;
import com.spring.step1.bean.vo.result.PageResult;
import com.spring.step1.mapper.UserRoleMapper;
import com.spring.step1.service.sample.UserRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 系统用户角色关系表 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Service
@Transactional
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleEntity> implements UserRoleService {

    /**
     * 系统用户角色关系表 服务实现类
     *
     * @param pageParams 系统用户角色关系表分页查询page对象
     * @param dto        系统用户角色关系表分页查询对象
     * @return 查询分页系统用户角色关系表返回对象
     */
    @Override
    public PageResult<UserRoleVo> getUserRolePage(Page<UserRoleEntity> pageParams, UserRoleDto dto) {
        IPage<UserRoleVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<UserRoleVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加系统用户角色关系表
     *
     * @param dto 系统用户角色关系表添加
     */
    @Override
    public void addUserRole(UserRoleDto dto) {
        UserRoleEntity userRole = new UserRoleEntity();
        BeanUtils.copyProperties(dto, userRole);
        save(userRole);
    }

    /**
     * 更新系统用户角色关系表
     *
     * @param dto 系统用户角色关系表更新
     */
    @Override
    public void updateUserRole(UserRoleDto dto) {
        UserRoleEntity userRole = new UserRoleEntity();
        BeanUtils.copyProperties(dto, userRole);
        updateById(userRole);
    }

    /**
     * 删除|批量删除系统用户角色关系表
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteUserRole(List<Long> ids) {
        removeByIds(ids);
    }
}