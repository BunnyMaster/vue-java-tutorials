package com.spring.official.service.system.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.official.domain.dto.system.UserDeptDto;
import com.spring.official.domain.entity.UserDeptEntity;
import com.spring.official.domain.vo.UserDeptVo;
import com.spring.official.domain.vo.result.PageResult;
import com.spring.official.mapper.UserDeptMapper;
import com.spring.official.service.system.UserDeptService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 部门用户关系表 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Service
@Transactional
public class UserDeptServiceImpl extends ServiceImpl<UserDeptMapper, UserDeptEntity> implements UserDeptService {

    /**
     * 部门用户关系表 服务实现类
     *
     * @param pageParams 部门用户关系表分页查询page对象
     * @param dto        部门用户关系表分页查询对象
     * @return 查询分页部门用户关系表返回对象
     */
    @Override
    public PageResult<UserDeptVo> getUserDeptPage(Page<UserDeptEntity> pageParams, UserDeptDto dto) {
        IPage<UserDeptVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<UserDeptVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加部门用户关系表
     *
     * @param dto 部门用户关系表添加
     */
    @Override
    public void addUserDept(UserDeptDto dto) {
        UserDeptEntity userDept = new UserDeptEntity();
        BeanUtils.copyProperties(dto, userDept);
        save(userDept);
    }

    /**
     * 更新部门用户关系表
     *
     * @param dto 部门用户关系表更新
     */
    @Override
    public void updateUserDept(UserDeptDto dto) {
        UserDeptEntity userDept = new UserDeptEntity();
        BeanUtils.copyProperties(dto, userDept);
        updateById(userDept);
    }

    /**
     * 删除|批量删除部门用户关系表
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteUserDept(List<Long> ids) {
        removeByIds(ids);
    }
}