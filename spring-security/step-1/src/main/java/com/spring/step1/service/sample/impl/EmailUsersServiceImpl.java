package com.spring.step1.service.sample.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.step1.bean.dto.system.EmailUsersDto;
import com.spring.step1.bean.entity.EmailUsersEntity;
import com.spring.step1.bean.vo.EmailUsersVo;
import com.spring.step1.bean.vo.result.PageResult;
import com.spring.step1.mapper.EmailUsersMapper;
import com.spring.step1.service.sample.EmailUsersService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 邮箱发送表 服务实现类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
@Service
@Transactional
public class EmailUsersServiceImpl extends ServiceImpl<EmailUsersMapper, EmailUsersEntity> implements EmailUsersService {

    /**
     * 邮箱发送表 服务实现类
     *
     * @param pageParams 邮箱发送表分页查询page对象
     * @param dto        邮箱发送表分页查询对象
     * @return 查询分页邮箱发送表返回对象
     */
    @Override
    public PageResult<EmailUsersVo> getEmailUsersPage(Page<EmailUsersEntity> pageParams, EmailUsersDto dto) {
        IPage<EmailUsersVo> page = baseMapper.selectListByPage(pageParams, dto);

        return PageResult.<EmailUsersVo>builder()
                .list(page.getRecords())
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加邮箱发送表
     *
     * @param dto 邮箱发送表添加
     */
    @Override
    public void addEmailUsers(EmailUsersDto dto) {
        EmailUsersEntity emailUsers = new EmailUsersEntity();
        BeanUtils.copyProperties(dto, emailUsers);
        save(emailUsers);
    }

    /**
     * 更新邮箱发送表
     *
     * @param dto 邮箱发送表更新
     */
    @Override
    public void updateEmailUsers(EmailUsersDto dto) {
        EmailUsersEntity emailUsers = new EmailUsersEntity();
        BeanUtils.copyProperties(dto, emailUsers);
        updateById(emailUsers);
    }

    /**
     * 删除|批量删除邮箱发送表
     *
     * @param ids 删除id列表
     */
    @Override
    public void deleteEmailUsers(List<Long> ids) {
        removeByIds(ids);
    }
}