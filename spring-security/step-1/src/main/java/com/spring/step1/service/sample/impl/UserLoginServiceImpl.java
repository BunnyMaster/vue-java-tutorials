package com.spring.step1.service.sample.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.step1.bean.dto.system.UserLoginDto;
import com.spring.step1.bean.entity.UserLoginEntity;
import com.spring.step1.bean.vo.UserLoginVo;
import com.spring.step1.bean.vo.result.PageResult;
import com.spring.step1.mapper.UserLoginMapper;
import com.spring.step1.service.sample.UserLoginService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户登录日志 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Service
@Transactional
public class UserLoginServiceImpl extends ServiceImpl<UserLoginMapper, UserLoginEntity> implements UserLoginService {

    /**
     * 用户登录日志 服务实现类
     *
     * @param pageParams 用户登录日志分页查询page对象
     * @param dto        用户登录日志分页查询对象
     * @return 查询分页用户登录日志返回对象
     */
    @Override
    public PageResult<UserLoginVo> getUserLoginPage(Page<UserLoginEntity> pageParams, UserLoginDto dto) {
        IPage<UserLoginVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<UserLoginVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加用户登录日志
     *
     * @param dto 用户登录日志添加
     */
    @Override
    public void addUserLogin(UserLoginDto dto) {
        UserLoginEntity userLogin = new UserLoginEntity();
        BeanUtils.copyProperties(dto, userLogin);
        save(userLogin);
    }

    /**
     * 更新用户登录日志
     *
     * @param dto 用户登录日志更新
     */
    @Override
    public void updateUserLogin(UserLoginDto dto) {
        UserLoginEntity userLogin = new UserLoginEntity();
        BeanUtils.copyProperties(dto, userLogin);
        updateById(userLogin);
    }

    /**
     * 删除|批量删除用户登录日志
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteUserLogin(List<Long> ids) {
        removeByIds(ids);
    }
}