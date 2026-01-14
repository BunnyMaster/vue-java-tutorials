package com.spring.step3.service.user.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.step3.domain.dto.user.AssignUserRoleDto;
import com.spring.step3.domain.dto.user.UserRoleDto;
import com.spring.step3.domain.entity.UserRoleEntity;
import com.spring.step3.domain.vo.UserRoleVo;
import com.spring.step3.domain.vo.result.PageResult;
import com.spring.step3.mapper.UserRoleMapper;
import com.spring.step3.service.user.UserRoleService;
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
     * 用户角色关联表 服务实现类
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

    /**
     * 根据用户id获取当前用户角色列表
     *
     * @param userId 用户id
     * @return 用户和角色列表
     */
    @Override
    public List<UserRoleVo> getRoleListByUserId(Long userId) {
        List<UserRoleEntity> userRoleEntityList = baseMapper.getRoleListByUserId(userId);
        return userRoleEntityList.stream().map(userRoleEntity -> {
                    UserRoleVo userRoleVo = new UserRoleVo();
                    BeanUtils.copyProperties(userRoleEntity, userRoleVo);
                    return userRoleVo;
                })
                .toList();
    }

    /**
     * 根据用户id分配用户角色
     *
     * @param dto 用户分配角色DTO {@link AssignUserRoleDto}
     */
    @Override
    public void assignUserRole(AssignUserRoleDto dto) {
        Long userId = dto.getUserId();
        List<Long> roleIds = dto.getRoleIds();

        // 先删除已经分配的角色
        baseMapper.deleteByUserId(userId);

        // 为用户分配角色
        List<UserRoleEntity> entityList = roleIds.stream().map(roleId -> {
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setUserId(userId);
            userRoleEntity.setRoleId(roleId);
            return userRoleEntity;
        }).toList();
        saveBatch(entityList);
    }
}