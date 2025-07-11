package com.spring.step2.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.step2.domain.dto.UsersDto;
import com.spring.step2.domain.entity.UsersEntity;
import com.spring.step2.domain.vo.UsersVo;
import com.spring.step2.domain.vo.result.PageResult;
import com.spring.step2.mapper.UsersMapper;
import com.spring.step2.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-11 14:49:46
 */
@DS("testJwt")
@Service
@Transactional
@RequiredArgsConstructor
public class UsersServiceImpl extends ServiceImpl<UsersMapper, UsersEntity> implements UsersService {

    private final PasswordEncoder passwordEncoder;

    /**
     * *  服务实现类
     *
     * @param pageParams 分页查询page对象
     * @param dto        分页查询对象
     * @return 查询分页返回对象
     */
    @Override
    public PageResult<UsersVo> getUsersPage(Page<UsersEntity> pageParams, UsersDto dto) {
        IPage<UsersVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<UsersVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加
     *
     * @param dto 添加
     */
    @Override
    public void addUsers(UsersDto dto) {
        UsersEntity user = new UsersEntity();
        BeanUtils.copyProperties(dto, user);

        // 设置用户密码
        String password = user.getPassword();
        String encodePassword = passwordEncoder.encode(password);
        user.setPassword(encodePassword);

        save(user);
    }

    /**
     * 更新
     *
     * @param dto 更新
     */
    @Override
    public void updateUsers(UsersDto dto) {
        UsersEntity users = new UsersEntity();
        BeanUtils.copyProperties(dto, users);
        updateById(users);
    }

    /**
     * 删除|批量删除
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteUsers(List<Long> ids) {
        removeByIds(ids);
    }
}