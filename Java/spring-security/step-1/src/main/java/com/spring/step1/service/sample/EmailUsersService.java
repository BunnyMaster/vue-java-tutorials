package com.spring.step1.service.sample;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.step1.bean.dto.system.EmailUsersDto;
import com.spring.step1.bean.entity.EmailUsersEntity;
import com.spring.step1.bean.vo.EmailUsersVo;
import com.spring.step1.bean.vo.result.PageResult;

import java.util.List;

/**
 * <p>
 * 邮箱发送表 服务类
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 12:02:29
 */
public interface EmailUsersService extends IService<EmailUsersEntity> {

    /**
     * 分页查询邮箱发送表
     *
     * @return {@link EmailUsersVo}
     */
    PageResult<EmailUsersVo> getEmailUsersPage(Page<EmailUsersEntity> pageParams, EmailUsersDto dto);

    /**
     * 添加邮箱发送表
     *
     * @param dto {@link EmailUsersDto} 添加表单
     */
    void addEmailUsers(EmailUsersDto dto);

    /**
     * 更新邮箱发送表
     *
     * @param dto {@link EmailUsersDto} 更新表单
     */
    void updateEmailUsers(EmailUsersDto dto);

    /**
     * 删除|批量删除邮箱发送表类型
     *
     * @param ids 删除id列表
     */
    void deleteEmailUsers(List<Long> ids);
}
